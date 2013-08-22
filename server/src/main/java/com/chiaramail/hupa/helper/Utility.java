package com.chiaramail.hupa.helper;

 import static com.chiaramail.hupa.helper.Account.LICENSE_ACTIVE;
import static com.chiaramail.hupa.helper.Account.LICENSE_EXPIRED;
import static com.chiaramail.hupa.helper.Account.LICENSE_UNKNOWN;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Utility {
    /**
     * Regular expression that represents characters we won't allow in file
     * names.
     * 
     * <p>
     * Allowed are:
     * <ul>
     * <li>word characters (letters, digits, and underscores): {@code \w}</li>
     * <li>spaces: {@code " "}</li>
     * <li>special characters: {@code !}, {@code #}, {@code $}, {@code %},
     * {@code &}, {@code '}, {@code (}, {@code )}, {@code -}, {@code @},
     * {@code ^}, {@code `}, <code>&#123;</code>, <code>&#125;</code>, {@code ~}, {@code .}, {@code ,}</li>
     * </ul>
     * </p>
     * 
     * @see #sanitizeFilename(String)
     */
    private static final String INVALID_CHARACTERS = "[^\\w !#$%&'()\\-@\\^`{}~.,]+";

    /**
     * Invalid characters in a file name are replaced by this character.
     * 
     * @see #sanitizeFilename(String)
     */
    private static final String REPLACEMENT_CHARACTER = "_";

    // \u00A0 (non-breaking space) happens to be used by French MUA

    // Note: no longer using the ^ beginning character combined with (...)+
    // repetition matching as we might want to strip ML tags. Ex:
    // Re: [foo] Re: RE : [foo] blah blah blah
    private static final Pattern RESPONSE_PATTERN = Pattern.compile(
            "((Re|Fw|Fwd|Aw|R\\u00E9f\\.)(\\[\\d+\\])?[\\u00A0 ]?: *)+",
            Pattern.CASE_INSENSITIVE);

    /**
     * Mailing-list tag pattern to match strings like "[foobar] "
     */
    private static final Pattern TAG_PATTERN = Pattern.compile(
            "\\[[-_a-z0-9]+\\] ", Pattern.CASE_INSENSITIVE);
    public static final String CONTENT_SERVER_APP = "/DynamicContentServer/ContentServer";
    public static final String RECEIVE_CONTENT = "RECEIVE CONTENT ";
    public static final String UPDATE_CONTENT = "UPDATE CONTENT ";
    public static final String FETCH_CONTENT = "FETCH CONTENT ";
    public static final String DELETE_CONTENT = "DELETE CONTENT ";
    public static final String DELETE_DATA = "DELETE DATA ";
    public static final String REMOVE_RECIPIENT = "REMOVE RECIPIENT ";
    public static final String GET_DATA = "GET DATA ";
    public static final String USER_REGISTERED = "USER REGISTERED ";
    public static final String SERVER_LICENSED = "SERVER LICENSED ";
    // private static final int MAX_SIZE = 32768;
    // private static final int MAX_SIZE = 7844;
    // private static final int MAX_SIZE = 128;
    private static final int MAX_SIZE = 76;
    private static final int DECODED_SIZE = 57;
    private static final int NEXT_DAY = 24 * 60 * 60 * 1000; // Number of msec
                                                             // in a day

    public static final String BLANK = " ";
    public static final String CONTENT_SERVER_NAME = "X-ChiaraMail-Content-Server-Name";
    public static final String CONTENT_SERVER_PORT = "X-ChiaraMail-Content-Server-Port";
    public static final String CONTENT_POINTER = "X-ChiaraMail-Content-Pointer";
    public static final String ENCRYPTION_KEY = "X-ChiaraMail-Content-Key2";
    public static final String CONTENT_DURATION = "X-ChiaraMail-Content-Duration";
    public static final String DEFAULT_CONTENT_SERVER_NAME = "www.chiaramail.com";
    public static final String DEFAULT_CONTENT_SERVER_PORT = "443";

    public static final String GREEN = "#00a000";
    public static final String RED = "#ff0000";
    public static final String BLACK = "#000000";

    public static Vector ValidECSMessages = new Vector();
    public static Vector BogusECSMessages = new Vector();

    public static boolean arrayContains(Object[] a, Object o) {
        for (Object element : a) {
            if (element.equals(o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean arrayContainsAny(Object[] a, Object... o) {
        for (Object element : a) {
            if (arrayContains(o, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Combines the given array of Objects into a single String using each
     * Object's toString() method and the separator character between each part.
     * 
     * @param parts
     * @param separator
     * @return new String
     */
    public static String combine(Object[] parts, char separator) {
        if (parts == null) {
            return null;
        } else if (parts.length == 0) {
            return "";
        } else if (parts.length == 1) {
            return parts[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parts[0]);
        for (int i = 1; i < parts.length; ++i) {
            sb.append(separator);
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public static String base64Decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        byte[] decoded = new Base64().decode(encoded.getBytes());
        return new String(decoded);
    }

    public static byte[] base64DecodeToBytes(String encoded) {
        if (encoded == null) {
            return null;
        }
        byte[] decoded = new Base64().decode(encoded.getBytes());
        return decoded;
    }

    public static String base64Encode(String s) {
        if (s == null) {
            return s;
        }
        byte[] encoded = new Base64().encode(s.getBytes());
        return new String(encoded);
    }

    public static boolean domainFieldValid(String s) {
        if (s != null) {
            if (s.matches("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)*[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?$")
                    && s.length() <= 253) {
                return true;
            }
            if (s.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
                return true;
            }
        }
        return false;
    }

    private static final Pattern ATOM = Pattern
            .compile("^(?:[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]|\\s)+$");

    /**
     * Quote a string, if necessary, based upon the definition of an "atom," as
     * defined by RFC2822 (http://tools.ietf.org/html/rfc2822#section-3.2.4).
     * Strings that consist purely of atoms are left unquoted; anything else is
     * returned as a quoted string.
     * 
     * @param text
     *            String to quote.
     * @return Possibly quoted string.
     */
    public static String quoteAtoms(final String text) {
        if (ATOM.matcher(text).matches()) {
            return text;
        } else {
            return quoteString(text);
        }
    }

    /**
     * Ensures that the given string starts and ends with the double quote
     * character. The string is not modified in any way except to add the double
     * quote character to start and end if it's not already there. sample ->
     * "sample" "sample" -> "sample" ""sample"" -> "sample"
     * "sample"" -> "sample" sa"mp"le -> "sa"mp"le" "sa"mp"le" -> "sa"mp"le"
     * (empty string) -> "" " -> ""
     * 
     * @param s
     * @return
     */
    public static String quoteString(String s) {
        if (s == null) {
            return null;
        }
        if (!s.matches("^\".*\"$")) {
            return "\"" + s + "\"";
        } else {
            return s;
        }
    }

    /**
     * A fast version of URLDecoder.decode() that works only with UTF-8 and does
     * only two allocations. This version is around 3x as fast as the standard
     * one and I'm using it hundreds of times in places that slow down the UI,
     * so it helps.
     */
    public static String fastUrlDecode(String s) {
        try {
            byte[] bytes = s.getBytes("UTF-8");
            byte ch;
            int length = 0;
            for (int i = 0, count = bytes.length; i < count; i++) {
                ch = bytes[i];
                if (ch == '%') {
                    int h = (bytes[i + 1] - '0');
                    int l = (bytes[i + 2] - '0');
                    if (h > 9) {
                        h -= 7;
                    }
                    if (l > 9) {
                        l -= 7;
                    }
                    bytes[length] = (byte) ((h << 4) | l);
                    i += 2;
                } else if (ch == '+') {
                    bytes[length] = ' ';
                } else {
                    bytes[length] = bytes[i];
                }
                length++;
            }
            return new String(bytes, 0, length, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    /**
     * <p>
     * Wraps a multiline string of text, identifying words by <code>' '</code>.
     * </p>
     * 
     * <p>
     * New lines will be separated by the system property line separator. Very
     * long words, such as URLs will <i>not</i> be wrapped.
     * </p>
     * 
     * <p>
     * Leading spaces on a new line are stripped. Trailing spaces are not
     * stripped.
     * </p>
     * 
     * <pre>
     * WordUtils.wrap(null, *) = null
     * WordUtils.wrap("", *) = ""
     * </pre>
     * 
     * Adapted from the Apache Commons Lang library.
     * http://svn.apache.org/viewvc/commons/proper/lang
     * /trunk/src/main/java/org/apache/commons/lang3/text/WordUtils.java SVN
     * Revision 925967, Mon Mar 22 06:16:49 2010 UTC
     * 
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements. See the NOTICE file distributed with this
     * work for additional information regarding copyright ownership. The ASF
     * licenses this file to You under the Apache License, Version 2.0 (the
     * "License"); you may not use this file except in compliance with the
     * License. You may obtain a copy of the License at
     * 
     * http://www.apache.org/licenses/LICENSE-2.0
     * 
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
     * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
     * License for the specific language governing permissions and limitations
     * under the License.
     * 
     * @param str
     *            the String to be word wrapped, may be null
     * @param wrapLength
     *            the column to wrap the words at, less than 1 is treated as 1
     * @return a line with newlines inserted, <code>null</code> if null input
     */
    private static final String NEWLINE_REGEX = "(?:\\r?\\n)";

    public static String wrap(String str, int wrapLength) {
        StringBuilder result = new StringBuilder();
        for (String piece : str.split(NEWLINE_REGEX)) {
            result.append(wrap(piece, wrapLength, null, false));
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * <p>
     * Wraps a single line of text, identifying words by <code>' '</code>.
     * </p>
     * 
     * <p>
     * Leading spaces on a new line are stripped. Trailing spaces are not
     * stripped.
     * </p>
     * 
     * <pre>
     * WordUtils.wrap(null, *, *, *) = null
     * WordUtils.wrap("", *, *, *) = ""
     * </pre>
     * 
     * This is from the Apache Commons Lang library.
     * http://svn.apache.org/viewvc/commons/proper/lang
     * /trunk/src/main/java/org/apache/commons/lang3/text/WordUtils.java SVN
     * Revision 925967, Mon Mar 22 06:16:49 2010 UTC
     * 
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements. See the NOTICE file distributed with this
     * work for additional information regarding copyright ownership. The ASF
     * licenses this file to You under the Apache License, Version 2.0 (the
     * "License"); you may not use this file except in compliance with the
     * License. You may obtain a copy of the License at
     * 
     * http://www.apache.org/licenses/LICENSE-2.0
     * 
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
     * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
     * License for the specific language governing permissions and limitations
     * under the License.
     * 
     * @param str
     *            the String to be word wrapped, may be null
     * @param wrapLength
     *            the column to wrap the words at, less than 1 is treated as 1
     * @param newLineStr
     *            the string to insert for a new line, <code>null</code> uses
     *            the system property line separator
     * @param wrapLongWords
     *            true if long words (such as URLs) should be wrapped
     * @return a line with newlines inserted, <code>null</code> if null input
     */
    public static String wrap(String str, int wrapLength, String newLineStr,
            boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = "\n";
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

        while ((inputLineLength - offset) > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }
            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

            if (spaceToWrapAt >= offset) {
                // normal case
                wrappedLine.append(str.substring(offset, spaceToWrapAt));
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;
            } else {
                // really long word or URL
                if (wrapLongWords) {
                    // wrap really long word one line at a time
                    wrappedLine.append(str.substring(offset, wrapLength
                            + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    // do not wrap really long word, just extend beyond limit
                    spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                    if (spaceToWrapAt >= 0) {
                        wrappedLine
                                .append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }

        // Whatever is left in line is short enough to just pass through
        wrappedLine.append(str.substring(offset));

        return wrappedLine.toString();
    }

    /**
     * Extract the 'original' subject value, by ignoring leading
     * response/forward marker and '[XX]' formatted tags (as many mailing-list
     * softwares do).
     * 
     * <p>
     * Result is also trimmed.
     * </p>
     * 
     * @param subject
     *            Never <code>null</code>.
     * @return Never <code>null</code>.
     */
    public static String stripSubject(final String subject) {
        int lastPrefix = 0;

        final Matcher tagMatcher = TAG_PATTERN.matcher(subject);
        String tag = null;
        // whether tag stripping logic should be active
        boolean tagPresent = false;
        // whether the last action stripped a tag
        boolean tagStripped = false;
        if (tagMatcher.find(0)) {
            tagPresent = true;
            if (tagMatcher.start() == 0) {
                // found at beginning of subject, considering it an actual tag
                tag = tagMatcher.group();

                // now need to find response marker after that tag
                lastPrefix = tagMatcher.end();
                tagStripped = true;
            }
        }

        final Matcher matcher = RESPONSE_PATTERN.matcher(subject);

        // while:
        // - lastPrefix is within the bounds
        // - response marker found at lastPrefix position
        // (to make sure we don't catch response markers that are part of
        // the actual subject)

        while (lastPrefix < subject.length() - 1
                && matcher.find(lastPrefix)
                && matcher.start() == lastPrefix
                && (!tagPresent || tag == null || subject.regionMatches(
                        matcher.end(), tag, 0, tag.length()))) {
            lastPrefix = matcher.end();

            if (tagPresent) {
                tagStripped = false;
                if (tag == null) {
                    // attempt to find tag
                    if (tagMatcher.start() == lastPrefix) {
                        tag = tagMatcher.group();
                        lastPrefix += tag.length();
                        tagStripped = true;
                    }
                } else if (lastPrefix < subject.length() - 1
                        && subject.startsWith(tag, lastPrefix)) {
                    // Re: [foo] Re: [foo] blah blah blah
                    // ^ ^
                    // ^ ^
                    // ^ new position
                    // ^
                    // initial position
                    lastPrefix += tag.length();
                    tagStripped = true;
                }
            }
        }
        // Null pointer check is to make the static analysis component of
        // Eclipse happy.
        if (tagStripped && (tag != null)) {
            // restore the last tag
            lastPrefix -= tag.length();
        }
        if (lastPrefix > -1 && lastPrefix < subject.length() - 1) {
            return subject.substring(lastPrefix).trim();
        } else {
            return subject.trim();
        }
    }

    /**
     * @param parentDir
     * @param name
     *            Never <code>null</code>.
     */
    public static void touchFile(final File parentDir, final String name) {
        final File file = new File(parentDir, name);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.setLastModified(System.currentTimeMillis());
            }
        } catch (Exception e) {
            System.out.println("Unable to touch file: "
                    + file.getAbsolutePath() + " " + e.getMessage());
        }
    }

    /**
     * Creates a unique file in the given directory by appending a hyphen and a
     * number to the given filename.
     * 
     * @param directory
     * @param filename
     * @return
     */
    public static File createUniqueFile(File directory, String filename) {
        File file = new File(directory, filename);
        if (!file.exists()) {
            return file;
        }
        // Get the extension of the file, if any.
        int index = filename.lastIndexOf('.');
        String format;
        if (index != -1) {
            String name = filename.substring(0, index);
            String extension = filename.substring(index);
            format = name + "-%d" + extension;
        } else {
            format = filename + "-%d";
        }
        for (int i = 2; i < Integer.MAX_VALUE; i++) {
            file = new File(directory, String.format(format, i));
            if (!file.exists()) {
                return file;
            }
        }
        return null;
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean move(final File from, final File to) {
        if (to.exists()) {
            to.delete();
        }
        to.getParentFile().mkdirs();

        try {
            FileInputStream in = new FileInputStream(from);
            try {
                FileOutputStream out = new FileOutputStream(to);
                try {
                    byte[] buffer = new byte[1024];
                    int count = -1;
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                    }
                } finally {
                    out.close();
                }
            } finally {
                try {
                    in.close();
                } catch (Throwable ignore) {
                }
            }
            from.delete();
            return true;
        } catch (Exception e) {
            System.out.println("cannot move " + from.getAbsolutePath() + " to "
                    + to.getAbsolutePath() + " " + e.getMessage());
            return false;
        }

    }

    /**
     * @param fromDir
     * @param toDir
     */
    public static void moveRecursive(final File fromDir, final File toDir) {
        if (!fromDir.exists()) {
            return;
        }
        if (!fromDir.isDirectory()) {
            if (toDir.exists()) {
                if (!toDir.delete()) {
                    System.out
                            .println("cannot delete already existing file/directory "
                                    + toDir.getAbsolutePath());
                }
            }
            if (!fromDir.renameTo(toDir)) {
                System.out.println("cannot rename " + fromDir.getAbsolutePath()
                        + " to " + toDir.getAbsolutePath()
                        + " - moving instead");
                move(fromDir, toDir);
            }
            return;
        }
        if (!toDir.exists() || !toDir.isDirectory()) {
            if (toDir.exists()) {
                toDir.delete();
            }
            if (!toDir.mkdirs()) {
                System.out.println("cannot create directory "
                        + toDir.getAbsolutePath());
            }
        }
        File[] files = fromDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                moveRecursive(file, new File(toDir, file.getName()));
                file.delete();
            } else {
                File target = new File(toDir, file.getName());
                if (!file.renameTo(target)) {
                    System.out.println("cannot rename "
                            + file.getAbsolutePath() + " to "
                            + target.getAbsolutePath() + " - moving instead");
                    move(file, target);
                }
            }
        }
        if (!fromDir.delete()) {
            System.out.println("cannot delete " + fromDir.getAbsolutePath());
        }
    }

    private static final String IMG_SRC_REGEX = "(?is:<img[^>]+src\\s*=\\s*['\"]?([a-z]+)\\:)";
    private static final Pattern IMG_PATTERN = Pattern.compile(IMG_SRC_REGEX);

    /**
     * Figure out if this part has images. TODO: should only return true if
     * we're an html part
     * 
     * @param message
     *            Content to evaluate
     * @return True if it has external images; false otherwise.
     */
    public static boolean hasExternalImages(final String message) {
        Matcher imgMatches = IMG_PATTERN.matcher(message);
        while (imgMatches.find()) {
            if (!imgMatches.group(1).equals("content")) {
                System.out.println("External images found");
                return true;
            }
        }
        System.out.println("No external images.");
        return false;
    }

    /**
     * Unconditionally close a Cursor. Equivalent to {@link Cursor#close()}, if
     * cursor is non-null. This is typically used in finally blocks.
     * 
     * @param cursor
     *            cursor to close
     */
    public static void closeQuietly(final Cursor cursor) {
        if (cursor != null) {
            // cursor.close();
        }
    }

    private static class BlockData {
        int blockSize;
        int bytesRead;
        String msgBlock;
        String msgChunk;
    }

    private static BlockData getNextBlock(BufferedReader rdr, char[] buf)
            throws IOException {
        BlockData blockData = new BlockData();
        String msgChunk = "";
        String msgBlock = "";
        int blockSize = 0, bytesRead = 0;

        while (blockSize < MAX_SIZE) {
            bytesRead = rdr.read(buf, 0, MAX_SIZE - bytesRead);
            if (bytesRead == -1)
                return null;
            msgChunk = new String(buf, 0, bytesRead);
            msgBlock += msgChunk;
            blockSize += bytesRead;
        }
        blockData.blockSize = blockSize;
        blockData.bytesRead = bytesRead;
        blockData.msgBlock = msgBlock;
        blockData.msgChunk = msgChunk;
        return blockData;
    }

    public static String fetchBodyContent(Message message, Account account,
            String[] contentPointers, String contentServerName,
            String contentServerPort, String encryptionKey) {
        try {
            String from = message.getFrom()[0].toString().replaceFirst("^.*<(.*)>.*$", "$1");
            String[] reply = doFetchContent(account,
                    from + BLANK
                            + contentPointers[0], "https://"
                            + contentServerName + ":" + contentServerPort,
                    account.getEmail(),
                    base64Encode(account.getPassword()),
                    false, null,
                    null);
            if (reply[0].equals("3")) {
                // Indicate to MessageList to set message Subject field color to
                // green in the message list.
                if (!ValidECSMessages
                        .contains(message.getHeader("Message-ID")[0]))
                    ValidECSMessages
                            .addElement(message.getHeader("Message-ID")[0]);
                String[] encryptionHeader = message.getHeader(ENCRYPTION_KEY);
                if (encryptionHeader != null) {
                    return new String(decrypt(encryptionKey.getBytes(),
                            base64DecodeToBytes(reply[1].substring(reply[1]
                                    .toUpperCase().indexOf("CONTENT = ")
                                    + "CONTENT = ".length()))));
                }
                return base64Decode(reply[1].substring(reply[1].toUpperCase()
                        .indexOf("CONTENT = ") + "CONTENT = ".length()));
            } else {
                // Indicate to MessageList to set message Subject field color to
                // red in the message list; the fetch had problems, so this
                // message may be bogus. Better safe than sorry.
                if (!BogusECSMessages
                        .contains(message.getHeader("Message-ID")[0]))
                    BogusECSMessages
                            .addElement(message.getHeader("Message-ID")[0]);
                // Toast.makeText(getContext(),
                // getContext().getString(R.string.message_read_error_fetching_content)
                // + reply[1], Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception when fetching message content: "
                    + " " + e.getMessage());
            e.printStackTrace();
            // Toast.makeText(this.getContext(),
            // getContext().getString(R.string.message_fetch_exception) + e,
            // Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /****************************************************************************/
    /* The askServer() method sends an HTTP request to a server. */
    /****************************************************************************/
    public static String[] askServer(String url_str, String command,
            String email_addr, String password, boolean isAttachment,
            String filename, String key) throws Exception {
        
//        System.out.println("AskServer: " + url_str + " cmd:" + command + " email:" + email_addr + " pass:" + password + " att:" + isAttachment + " file:" + filename + " key:" + key);
        String tmp = "", parms;

        HttpURLConnection connection;

        InputStream is;

        BufferedWriter writer;

        BufferedReader rdr;

        String[] srvr_rsp = null;

        URL url;

        int index;

        srvr_rsp = new String[2];

        try {
            url = new URL(url_str);
            if (command.startsWith(SERVER_LICENSED)) {
                // Since the request isn't going to the ChiaraMail content
                // server, make sure the private server has an active license.
                connection = (HttpsURLConnection) new URL("https://"
                        + DEFAULT_CONTENT_SERVER_NAME + ":"
                        + DEFAULT_CONTENT_SERVER_PORT + CONTENT_SERVER_APP)
                        .openConnection();
                connection.setDoOutput(true);
                writer = new BufferedWriter(new OutputStreamWriter(
                        connection.getOutputStream()));
                writer.write("email_addr="
                        + URLEncoder.encode(email_addr, "UTF-8")
                        + "&"
                        + "passwd="
                        + URLEncoder.encode(password, "UTF-8")
                        + "&"
                        + "cmd="
                        + SERVER_LICENSED
                        + "&"
                        + "parms="
                        + URLEncoder.encode(
                                url_str.substring(0,
                                        url_str.indexOf(CONTENT_SERVER_APP)),
                                "UTF-8"));
                writer.close();
                is = connection.getInputStream();

                rdr = new BufferedReader(new InputStreamReader(is));
                tmp = rdr.readLine();
                if ((index = tmp.indexOf(BLANK)) != -1) {
                    srvr_rsp[0] = tmp.substring(0, tmp.indexOf(BLANK));
                    srvr_rsp[1] = tmp.substring(tmp.indexOf(BLANK) + 1);
                } else {
                    srvr_rsp[0] = tmp;
                    srvr_rsp[1] = "";
                }
                rdr.close();
                is.close();
                return srvr_rsp;
                // if (!srvr_rsp[0].equals("10")) return srvr_rsp;
            }

            if (url_str.startsWith("https")) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setDoOutput(true);

            index = command.indexOf(BLANK);
            tmp = "";
            tmp += command.substring(0, index + 1);
            command = command.substring(index).trim();
            index = command.indexOf(BLANK);
            if (index == -1) {
                tmp += command;
                parms = "";
            } else {
                tmp += command.substring(0, index);
                parms = command.substring(index).trim();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    connection.getOutputStream()));
            writer.write("email_addr=" + URLEncoder.encode(email_addr, "UTF-8")
                    + "&" + "passwd=" + URLEncoder.encode(password, "UTF-8")
                    + "&" + "cmd=" + tmp + "&" + "parms="
                    + URLEncoder.encode(parms, "UTF-8"));
            writer.close();

            is = connection.getInputStream();
            rdr = new BufferedReader(new InputStreamReader(is));

            if (isAttachment) {
                char[] buf = new char[MAX_SIZE + 29]; // Include room for
                                                      // response from content
                                                      // server plus 76 bytes of
                                                      // Base64 encoded content
                byte[] blockArray = new byte[16 * DECODED_SIZE];
                byte[] decodedChunk;
                int bytesRead = 0;
                int totalBlockLen = 0;
                String msgBlock = "";

                File file = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + filename);
                if (file.exists())
                    file.delete();

                System.out.println("Creating attachment file: " + file.getAbsolutePath());
                
                RandomAccessFile attachmentFile = new RandomAccessFile(file,
                        "rw");
                bytesRead = rdr.read(buf);
                /**
                 * if (bytesRead < MAX_SIZE + 29) { // Should never happen
                 * attachmentFile.close(); rdr.close(); is.close(); return null;
                 * }
                 **/
                String tmp2 = new String(buf, 0, 29);
                if (!tmp2.startsWith("3 ")) {
                    tmp = new String(buf, 0, buf.length);
                    if ((index = tmp.indexOf(BLANK)) != -1) {
                        srvr_rsp[0] = tmp.substring(0, tmp.indexOf(BLANK));
                        srvr_rsp[1] = tmp.substring(tmp.indexOf(BLANK) + 1);
                    } else {
                        srvr_rsp[0] = tmp;
                        srvr_rsp[1] = "";
                    }
                    return srvr_rsp;
                }
                int startContentIndex = tmp2.toUpperCase()
                        .indexOf("CONTENT = ") + "CONTENT = ".length();
                tmp = tmp2.substring(0, startContentIndex);
                if (tmp.startsWith("3 ")) {
                    msgBlock = new String(buf).substring(startContentIndex);
                    decodedChunk = base64DecodeToBytes(msgBlock);
                    if (key != null) {
                        for (int i = 0; i < decodedChunk.length; i++) {
                            blockArray[i] = decodedChunk[i];
                        }
                        totalBlockLen = decodedChunk.length;
                    } else {
                        attachmentFile.write(decodedChunk);
                    }
                    buf = new char[MAX_SIZE];
                    bytesRead = 0;
                    while (bytesRead >= 0) {
                        if (key != null) {
                            /*
                             * The following code reads 76-byte data blocks from
                             * the content server, decodes them and concatenates
                             * them into a block whose lengths is divisible by
                             * 16, which is needed for decryption. Then result
                             * is decrypted prior to saving to disk.
                             */
                            do {
                                BlockData blockData = getNextBlock(rdr, buf);
                                if (blockData != null) {
                                    bytesRead = blockData.bytesRead;
                                    decodedChunk = base64DecodeToBytes(blockData.msgBlock);
                                    for (int i = 0; i < decodedChunk.length; i++) {
                                        blockArray[i + totalBlockLen] = decodedChunk[i];
                                    }
                                    totalBlockLen += decodedChunk.length;
                                    if (totalBlockLen % 16 == 0)
                                        break; // Read the next block from the
                                               // server
                                } else { // EOF, bytesRead == -1
                                    bytesRead = -1;
                                    break;
                                }
                            } while (totalBlockLen % 16 != 0);
                            if (totalBlockLen == 0)
                                break;
                            byte[] tmpArray = decrypt(key.getBytes(),
                                    blockArray);
                            blockArray = Arrays.copyOf(tmpArray, totalBlockLen);

                            attachmentFile.write(blockArray);
                            blockArray = new byte[16 * DECODED_SIZE];
                            totalBlockLen = 0;
                        } else {
                            BlockData blockData = getNextBlock(rdr, buf);
                            if (blockData == null)
                                break;
                            bytesRead = blockData.bytesRead;
                            decodedChunk = base64DecodeToBytes(blockData.msgBlock);
                            attachmentFile.write(decodedChunk);
                        }
                    }
                }
                attachmentFile.close();
            } else {
                tmp = rdr.readLine();
            }

            rdr.close();
            is.close();

            if ((index = tmp.indexOf(BLANK)) != -1) {
                srvr_rsp[0] = tmp.substring(0, tmp.indexOf(BLANK));
                srvr_rsp[1] = tmp.substring(tmp.indexOf(BLANK) + 1);
            } else {
                srvr_rsp[0] = tmp;
                srvr_rsp[1] = "";
            }
        } catch (MalformedURLException e) {
            srvr_rsp[0] = "-1";
            srvr_rsp[1] = e.getMessage();
            System.out
                    .println("MalformedURLException when sending content to server: "
                            + " " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            srvr_rsp[0] = "-1";
            srvr_rsp[1] = e.getMessage();
            System.out.println("IOException when sending content to server: "
                    + " " + e.getMessage());
            e.printStackTrace();
        }
        return srvr_rsp;
    }

    /****************************************************************************/
    /* The registerServlet() method sends an HTTP request to the Register */
    /* servlet. */
    /****************************************************************************/
    public static void registerServlet(Account account, String addr)
            throws Exception {
        HttpURLConnection connection;

        BufferedWriter writer;

        URL url;

        try {
            url = new URL("https://www.chiaramail.com/Register");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);

            writer = new BufferedWriter(new OutputStreamWriter(
                    connection.getOutputStream()));
            writer.write("email_addr="
                    + URLEncoder.encode(account.getEmail(), "UTF-8") + "&addr="
                    + URLEncoder.encode("!FGw38;_&hIoPDC6887", "UTF-8")
                    + "&username="
                    + URLEncoder.encode(account.getName(), "UTF-8") + "&city="
                    + "&zipcode=00000" + "&country="
                    + URLEncoder.encode("United States of America", "UTF-8"));
            writer.close();

            connection.getResponseCode();
        } catch (MalformedURLException e) {
            System.out
                    .println("MalformedURLException when sending registration request to server: "
                            + " " + e.getMessage());
        } catch (IOException e) {
            System.out
                    .println("IOException when sending registration request to server: "
                            + " " + e.getMessage());
        }

        return;
    }

    /****************************************************************************/
    /* doUpdateContent() sends updated content to the ContentServer. */
    /****************************************************************************/
    public static String[] doUpdateContent(String parms, String url_str,
            String email_addr, String password) throws Exception {
        String[] rsp;

        rsp = askServer(url_str + CONTENT_SERVER_APP, UPDATE_CONTENT + parms,
                email_addr, password, false, null, null);

        return rsp;
    }

    /****************************************************************************/
    /* doFetchContent() fetches content from the ContentServer. */
    /****************************************************************************/
    public static String[] doFetchContent(Account account, String parms,
            String url_str, String email_addr, String password,
            boolean isAttachment, String filename, String encryption_key)
            throws Exception {
        String[] rsp;

        if (!url_str.toLowerCase().startsWith(
                "https://" + DEFAULT_CONTENT_SERVER_NAME + ":"
                        + DEFAULT_CONTENT_SERVER_PORT)
                && (account.getLicenseStatus() == LICENSE_UNKNOWN || account
                        .getLicenseCheckDate() + NEXT_DAY < getCurrentDate())) {
            rsp = askServer(url_str + CONTENT_SERVER_APP, SERVER_LICENSED
                    + parms, email_addr, password, false, null, null);
            if (rsp[0].equals("10")) {
                account.setLicenseStatus(LICENSE_ACTIVE);
                account.setLicenseCheckDate(new Date().getTime());
                rsp = askServer(url_str + CONTENT_SERVER_APP, FETCH_CONTENT
                        + parms, email_addr, password, isAttachment, filename,
                        encryption_key);
            } else {
                account.setLicenseStatus(LICENSE_EXPIRED);
            }
        } else {
            rsp = askServer(url_str + CONTENT_SERVER_APP,
                    FETCH_CONTENT + parms, email_addr, password, isAttachment,
                    filename, encryption_key);
        }

        return rsp;
    }

    /****************************************************************************/
    /* doReceiveContent() requests the ContentServer to store content. */
    /****************************************************************************/
    public static String[] doReceiveContent(Account account, String parms,
            String url_str, String email_addr, String password)
            throws Exception {
        String[] rsp;

        if (!url_str.toLowerCase().startsWith(
                "https://" + DEFAULT_CONTENT_SERVER_NAME + ":"
                        + DEFAULT_CONTENT_SERVER_PORT)
                && (account.getLicenseStatus() == LICENSE_UNKNOWN || account
                        .getLicenseCheckDate() + NEXT_DAY < getCurrentDate())) {
            rsp = askServer(url_str + CONTENT_SERVER_APP, SERVER_LICENSED
                    + parms, email_addr, password, false, null, null);
            if (rsp[0].equals("10")) {
                account.setLicenseStatus(LICENSE_ACTIVE);
                account.setLicenseCheckDate(new Date().getTime());
                rsp = askServer(url_str + CONTENT_SERVER_APP, RECEIVE_CONTENT
                        + parms, email_addr, password, false, null, null);
            } else {
                account.setLicenseStatus(LICENSE_EXPIRED);
            }
        } else {
            rsp = askServer(url_str + CONTENT_SERVER_APP, RECEIVE_CONTENT
                    + parms, email_addr, password, false, null, null);
        }

        return rsp;
    }

    /****************************************************************************/
    /* doRemoveRecipient() requests the ContentServer to remove the recipient */
    /* from the access list of the named message. */
    /****************************************************************************/
    public static String[] doRemoveRecipient(String parms, String url_str,
            String email_addr, String password) throws Exception {
        String[] rsp;

        rsp = askServer(url_str + CONTENT_SERVER_APP, REMOVE_RECIPIENT + parms,
                email_addr, password, false, null, null);

        return rsp;
    }

    /****************************************************************************/
    /* doDeleteContent() requests the ContentServer to delete content and */
    /* content pointer entries in the content_indexes database table. */
    /****************************************************************************/
    public static String[] doDeleteContent(String parms, String url_str,
            String email_addr, String password) throws Exception {
        String[] rsp;

        rsp = askServer(url_str + CONTENT_SERVER_APP, DELETE_CONTENT + parms,
                email_addr, password, false, null, null);

        return rsp;
    }

    /****************************************************************************/
    /* doDeleteData() requests the ContentServer to delete content only. */
    /****************************************************************************/
    public static String[] doDeleteData(String parms, String url_str,
            String email_addr, String password) throws Exception {
        String[] rsp;

        rsp = askServer(url_str + CONTENT_SERVER_APP, DELETE_DATA + parms,
                email_addr, password, false, null, null);

        return rsp;
    }

    /****************************************************************************/
    /* doGetData() requests the ContentServer to return the amount of space */
    /* the user has left. */
    /****************************************************************************/
    public static String[] doGetData(String url_str, String email_addr,
            String password) throws Exception {
        String[] rsp;

        rsp = askServer(url_str + CONTENT_SERVER_APP, GET_DATA, email_addr,
                password, false, null, null);

        return rsp;
    }

    /****************************************************************************/
    /* isUserRegistered() requests the ContentServer to report if the given */
    /* account exists. */
    /****************************************************************************/
    public static String isUserRegistered(Account account, String address) {
        try {
            String[] reply = askServer(
                    "https://" + account.getContentServerName() + ":"
                            + account.getContentServerPort()
                            + CONTENT_SERVER_APP, USER_REGISTERED + address,
                    account.getEmail(),
                    base64Encode(account.getPassword()), false,
                    null, null);
            if (reply[0].equals("7")) {
                String rsp = reply[1].substring(reply[1].lastIndexOf("= ") + 2);
                return rsp.substring(0, rsp.length() - 1);
            } else {
                System.out.println(reply[1]);
                return "";
            }
        } catch (Exception e) {
            System.out
                    .println("Exception when fetching content server response: "
                            + " " + e.getMessage());
            return "";
        }
    }

    /****************************************************************************/
    /* doRegisterUser() registers the user for content service. */
    /****************************************************************************/
    public static void doRegisterUser(Account account, String addr)
            throws Exception {
        String[] rsp;

        registerServlet(account, addr);

        return;
    }

    /****************************************************************************/
    /* getCurrentDate() fetches the current date and is used when validating */
    /* licenses. */
    /****************************************************************************/
    private static long getCurrentDate() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Replace characters we don't allow in file names with a replacement
     * character.
     * 
     * @param filename
     *            The original file name.
     * 
     * @return The sanitized file name containing only allowed characters.
     */
    public static String sanitizeFilename(String filename) {
        return filename.replaceAll(INVALID_CHARACTERS, REPLACEMENT_CHARACTER);
    }

    /**
     * Check to see if we have network connectivity.
     * 
     * @param app
     *            Current application (Hint: see if your base class has a
     *            getApplication() method.)
     * @return true if we have connectivity, false otherwise.
     */
    public static boolean hasConnectivity(final Object app) {
        return true;
    }

    private static final Pattern MESSAGE_ID = Pattern.compile("<" + "(?:"
            + "[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]+"
            + "(?:\\.[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]+)*" + "|"
            + "\"(?:[^\\\\\"]|\\\\.)*\"" + ")" + "@" + "(?:"
            + "[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]+"
            + "(?:\\.[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]+)*" + "|"
            + "\\[(?:[^\\\\\\]]|\\\\.)*\\]" + ")" + ">");

    public static List<String> extractMessageIds(final String text) {
        List<String> messageIds = new ArrayList<String>();
        Matcher matcher = MESSAGE_ID.matcher(text);

        int start = 0;
        while (matcher.find(start)) {
            String messageId = text.substring(matcher.start(), matcher.end());
            messageIds.add(messageId);
            start = matcher.end();
        }

        return messageIds;
    }

    public static String extractMessageId(final String text) {
        Matcher matcher = MESSAGE_ID.matcher(text);

        if (matcher.find()) {
            return text.substring(matcher.start(), matcher.end());
        }

        return null;
    }

    public static String[] copyOf(String[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    public static String extractAddresses(Address[] toAddrs, Address[] ccAddrs,
            Address[] bccAddrs) {
        String tmp = "", tmp1 = "", tmp2 = "", tmp3 = "";

        StringTokenizer st;

        for (int i = 0; i < toAddrs.length; i++) {
            st = new StringTokenizer(toAddrs[i].toString(), " ,");
            for (int j = 0; st.hasMoreTokens(); j++) {
                tmp1 += st.nextToken() + ",";
            }
        }

        for (int i = 0; i < ccAddrs.length; i++) {
            st = new StringTokenizer(ccAddrs[i].toString(), " ,");

            for (int j = 0; st.hasMoreTokens(); j++) {
                tmp2 += st.nextToken() + ",";
            }
        }

        for (int i = 0; i < bccAddrs.length; i++) {
            st = new StringTokenizer(bccAddrs[i].toString(), " ,");

            while (st.hasMoreTokens()) {
                tmp3 += st.nextToken() + ",";
            }
        }
        if (tmp1.length() > 0) {
            tmp += tmp1;
            if (tmp2.length() > 0) {
                tmp += "," + tmp2;
                if (tmp3.length() > 0) {
                    tmp += "," + tmp3;
                    return tmp;
                }
                return tmp;
            } else {
                if (tmp3.length() > 0) {
                    tmp += "," + tmp3;
                    return tmp;
                }
                return tmp;
            }
        } else {
            if (tmp2.length() > 0) {
                tmp += tmp2;
                if (tmp3.length() > 0) {
                    tmp += "," + tmp3;
                    return tmp;
                }
                return tmp;
            } else {
                if (tmp3.length() > 0) {
                    return tmp3;
                }
            }
        }
        return tmp;
    }

    public static String generateEncryptionKey() {
        Random random = new Random();
        return (String.valueOf(random.nextLong()) + "01234567890123456789012345678901")
                .substring(0, 32); // Pad out to 32 bytes
    }
    
    public static byte[] doFinal(int mode, byte[] key, byte[] data) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
        try {
            cipher.init(mode, skeySpec);
        } catch (Exception e) {
            System.err.println("Error initializing Cipher: " + e.getMessage());
            System.err.println("Your java version is : " + System.getProperty("java.version") + " installed in: " + System.getProperty("java.home"));
            System.err.println("If you are using sun/oracle version, be sure you have installed 'Java Cryptography Extension (JCE)'");
            throw e;
        }
        return cipher.doFinal(data);
    }

    public static String encrypt(byte[] raw, byte[] clear) throws Exception {
        return new String(Base64.encodeBase64(doFinal(Cipher.ENCRYPT_MODE, raw, clear)));
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        return doFinal(Cipher.DECRYPT_MODE, raw, encrypted);
    }

    // MCM
    public static int getSpinnerIndex() {
        return -1;
    }

    public static boolean validateHeaders(Message message) {
        String contentServerName[] = null;
        String contentServerPort[] = null;
        String contentPointers[] = null;

        int port = 0, pointer = 0;

        try {
            if ((contentServerName = message.getHeader(CONTENT_SERVER_NAME)) == null
                    || (contentServerPort = message
                            .getHeader(CONTENT_SERVER_PORT)) == null
                    || (contentPointers = message.getHeader(CONTENT_POINTER)) == null)
                return false;

            if (contentServerName.length == 0) {
                // Toast.makeText(this.getContext(),
                // this.getContext().getString(R.string.message_compose_error_missing_content_server_name),
                // Toast.LENGTH_LONG).show();
                return false;
            }

            if (contentServerPort.length == 0) {
                // Toast.makeText(this.getContext(),
                // this.getContext().getString(R.string.message_compose_error_missing_content_server_port),
                // Toast.LENGTH_LONG).show();
                return false;
            } else {
                try {
                    port = Integer.parseInt(contentServerPort[0]);
                } catch (Exception e) {
                    // Toast.makeText(this.getContext(),
                    // this.getContext().getString(R.string.message_compose_error_bogus_content_server_port)
                    // + port, Toast.LENGTH_LONG).show();
                    return false;
                }
                if (port < 0) {
                    // Toast.makeText(this.getContext(),
                    // this.getContext().getString(R.string.message_compose_error_rangerr_content_server_port)
                    // + contentServerPort, Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if (contentPointers.length == 0) {
                // Toast.makeText(this.getContext(),
                // this.getContext().getString(R.string.message_compose_error_missing_content_pointers),
                // Toast.LENGTH_LONG).show();
                return false;
            } else {
                StringTokenizer st = new StringTokenizer(contentPointers[0]);
                for (; st.hasMoreTokens();) {
                    try {
                        pointer = Integer.parseInt(st.nextToken());
                    } catch (Exception e) {
                        // Toast.makeText(this.getContext(),
                        // this.getContext().getString(R.string.message_compose_error_bogus_content_pointer)
                        // + pointer, Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (pointer < 0 || pointer % 8 != 0) {
                        // Toast.makeText(this.getContext(),
                        // this.getContext().getString(R.string.message_compose_error_bogus_content_pointer)
                        // + pointer, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    public static long getCurrentFreeMemoryBytes() {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapRemaining = Runtime.getRuntime().freeMemory();
        long nativeUsage = 0; // Debug.getNativeHeapAllocatedSize();

        return Runtime.getRuntime().maxMemory() - (heapSize - heapRemaining)
                - nativeUsage;
    }
}