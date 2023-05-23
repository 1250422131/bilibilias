package com.imcys.bilibilias.home.ui.fragment;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class TokenUtils {
    private static String requestToken = "";

    private static String md5(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getBiliMixin(String val) {

        if (!requestToken.equals("")) {
            return requestToken;
        }
        int[] OE = {46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45,
                35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38,
                41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60,
                51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36,
                20, 34, 44, 52};

        StringBuilder requestTokenBuilder = new StringBuilder();
        for (int v : OE) {
            requestTokenBuilder.append(val.charAt(v));
        }
        requestToken = requestTokenBuilder.toString().substring(0, 32);
        return requestToken;
    }

    public static String genBiliSign(Map<String, String> params, String secret) throws UnsupportedEncodingException {
        long wts = System.currentTimeMillis() / 1000;
        params.put("wts", Long.toString(wts));
        Map<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder dataStrBuilder = new StringBuilder();
        for (String k : sortedParams.keySet()) {
            dataStrBuilder.append(k).append("=").append(sortedParams.get(k)).append("&");
        }
        String dataStr = dataStrBuilder.substring(0, dataStrBuilder.length() - 1);

        dataStr += (secret);
        params.put("w_rid", md5(dataStr));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return sb.toString();
    }


}
