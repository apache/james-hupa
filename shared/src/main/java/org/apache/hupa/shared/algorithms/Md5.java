package org.apache.hupa.shared.algorithms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {
    public static String md5Hex(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(input.getBytes());
            byte messageDigest[] = md5.digest();
            String ret = "";
            for (byte b : messageDigest) {
                ret += Integer.toHexString((int)b & 0xff | 0x100).substring(1);
            }
            return ret;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
