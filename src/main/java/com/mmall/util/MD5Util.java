package com.mmall.util;

import java.security.MessageDigest;

public class MD5Util {

    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
            sb.append(byteToHexString(bytes[i]));
        return sb.toString();
    }

    private static String MD5Encode(String origin, String charsetName) {
        String ans = null;
        try {
            ans = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName))
                ans = byteArrayToHexString(md.digest(ans.getBytes()));
            else
                ans = byteArrayToHexString(md.digest(ans.getBytes(charsetName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        origin = origin + PropertiesUtil.getProperty("password.salt", "");
        return MD5Encode(origin, "utf-8");
    }


}
