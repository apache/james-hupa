package org.apache.hupa.server.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hupa.shared.data.SettingsImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SettingsDiscoverer {

    private static HashMap<String, Settings> validConfigs = new HashMap<String, Settings>();

    @Inject private Provider<Settings> settingsProvider;

    public Settings discoverSettings(String email) {

        if (!email.matches("^(.*<)?[A-Za-z0-9._%'*/=+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(>)?\\s*$")) {
            return new SettingsImpl();
        }

        String domain = email.replaceFirst("^.*@", "");

        Settings s = validConfigs.get(domain);
        if (s != null) {
            return s;
        }

        String [] mxHosts = null;
        try {
            mxHosts = lookupMailHosts(domain);
        } catch (Exception e) {
            mxHosts = new String[0];
        }

        s = settingsProvider != null ? settingsProvider.get() : new SettingsImpl();

        if (email.matches(".*@gmail.com") || isValidMx(".*google.*.com", mxHosts)) {
            s.setImapServer("imap.gmail.com");
            s.setImapPort(993);
            s.setImapSecure(true);
            s.setSmtpServer("smtp.gmail.com");
            s.setSmtpPort(587); //465
            s.setSmtpSecure(true);
            s.setSmtpAuth(true);
            s.setInboxFolderName("INBOX");
            s.setSentFolderName("[Gmail]/Sent");
            s.setTrashFolderName("[Gmail]/Trash");
            s.setDraftsFolderName("[Gmail]/Drafts");
        } else if (email.matches(".*@(yahoo|ymail)\\....?") || isValidMx(".*google.*\\....?", mxHosts)) {
            s.setImapServer("imap.mail.yahoo.com");
            s.setImapPort(993);
            s.setImapSecure(true);
            s.setSmtpServer("smtp.mail.yahoo.com");
            s.setSmtpPort(465);
            s.setSmtpSecure(true);
            s.setSmtpAuth(true);
            s.setInboxFolderName("INBOX");
            s.setSentFolderName("Sent");
            s.setTrashFolderName("Trash");
            s.setDraftsFolderName("Templates");
        } else if (email.matches(".*@(hotmail|outlook|live).com")) {
            s.setImapServer("imap-mail.outlook.com");
            s.setImapPort(993);
            s.setImapSecure(true);
            s.setSmtpServer("smtp-mail.outlook.com");
            s.setSmtpPort(587);
            s.setSmtpSecure(true);
            s.setSmtpAuth(true);
            s.setInboxFolderName("INBOX");
            s.setSentFolderName("Sent");
            s.setTrashFolderName("Trash");
            s.setDraftsFolderName("Templates");
        } else {
            String[] hostNames = new String[]{"imap." + domain, "smtp." + domain, "www." + domain, "mail." + domain, domain};
            String[] hosts = (String[])ArrayUtils.addAll(hostNames, mxHosts);

            Integer[] imapPorts = new Integer[]{993, 585, 143};
            Integer[] smtpPorts = new Integer[]{465, 587, 25};
            Integer[] ports = (Integer[])ArrayUtils.addAll(imapPorts, smtpPorts);

            final List<String> validPorts = new ArrayList<String>();
            ExecutorService es = Executors.newCachedThreadPool();
            for (final String h : hosts) {
                if (isValidHostName(h)) {
                    for (final Integer p : ports) {
                        es.execute(new Runnable() {
                            public void run() {
                                if (isValidPort(h, p)) {
                                    validPorts.add(h + ":" + p);
                                }
                            }
                        });
                    }
                }
            }

            try {
                es.awaitTermination(1500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ValidPorts: " + domain + " -> "+ validPorts);

            boolean imapdone = false;
            loop: for (final String h : hosts) {
                for (final Integer p : imapPorts) {
                    if (validPorts.contains(h + ":" + p)) {
                        s.setImapServer(h);
                        s.setImapPort(p);
                        s.setImapSecure(p != 143);
                        imapdone = true;
                        break loop;
                    }
               }
            }
            if (!imapdone) {
                s.setImapServer("");
                s.setImapPort(0);
                s.setImapSecure(false);
            }

            boolean smtpdone = false;
            loop: for (final String h : hosts) {
                for (final Integer p : smtpPorts) {
                    if (validPorts.contains(h + ":" + p)) {
                        s.setSmtpServer(h);
                        s.setSmtpPort(p);
                        s.setSmtpSecure(p != 25);
                        smtpdone = true;
                        break loop;
                    }
               }
            }
            if (!smtpdone) {
                s.setSmtpServer("");
                s.setSmtpPort(0);
                s.setSmtpSecure(false);
            }

            s.setSmtpAuth(true);
            s.setInboxFolderName("INBOX");
            s.setSentFolderName("Sent");
            s.setTrashFolderName("Trash");
            s.setDraftsFolderName("Drafts");
            System.out.println("Returning config: \n" + s);
        }
        return s;
    }

    static boolean isValidMx(String regexp, String[] mailhosts) {
        if (mailhosts != null) for (String h : mailhosts) {
            if (h.toLowerCase().matches(regexp)) {
                return true;
            }
        }
        return false;
    }

    static boolean isValidHostName(String name) {
        try {
            InetAddress.getByName(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isValidPort(String hostname, int port) {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(1500);
            socket.connect(new InetSocketAddress(hostname, port), 1500);
            socket.close();
            return true;
          } catch (Exception ex) {
            return false;
          }
    }

    static String[] lookupMailHosts(String domainName) throws NamingException {
        InitialDirContext iDirC = new InitialDirContext();
        Attributes attributes = iDirC.getAttributes("dns:/" + domainName,
                new String[] { "MX" });
        Attribute attributeMX = attributes.get("MX");
        if (attributeMX == null) {
            return (new String[] { domainName });
        }

        // split MX RRs into Preference Values(pvhn[0]) and Host Names(pvhn[1])
        String[][] pvhn = new String[attributeMX.size()][2];
        for (int i = 0; i < attributeMX.size(); i++) {
            pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
        }

        // sort the MX RRs by RR value (lower is preferred)
        Arrays.sort(pvhn, new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                return (Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]));
            }
        });

        // put sorted host names in an array, get rid of any trailing '.'
        String[] sortedHostNames = new String[pvhn.length];
        for (int i = 0; i < pvhn.length; i++) {
            sortedHostNames[i] = pvhn[i][1].endsWith(".") ? pvhn[i][1]
                    .substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
        }
        return sortedHostNames;
    }

    public void setValidSettings(User user) {
        String domain = user.getName().replaceFirst("^.*@", "");
        validConfigs.put(domain, user.getSettings());
    }

}
