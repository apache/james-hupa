package com.chiaramail.hupa.helper;

import java.util.Date;

public class Account {
    
    public static final int LICENSE_UNKNOWN = 0;
    public static final int LICENSE_ACTIVE = 1;
    public static final int LICENSE_EXPIRED = 2;
    
    private int license = LICENSE_UNKNOWN;
    private long time = System.currentTimeMillis() + (24*60*60*1000);
    public String email;
    public String password;
    public String name;
    public String serverName = "www.chiaramail.com";
    public String serverPort = "443";

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public void setLicenseStatus(int licenseActive) {
        license = licenseActive;
    }
    public int getLicenseStatus() {
        return license;
    }
    public void setLicenseCheckDate(long time) {
        this.time = time;
    }
    public int getLicenseCheckDate() {
        return (int)time;
    }
    public String getPassword() {
        return password;
    }
    public String getContentServerName() {
        return serverName;
    }
    public String getContentServerPort() {
        return serverPort;
    }

}
