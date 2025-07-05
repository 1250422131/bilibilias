package com.imcys.bilibilias.common.utils;


import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URLEncoder;
import java.util.TreeMap;
public class BiliAppSigner {
    public static final String APP_KEY = "27eb53fc9058f8c3";
    public static final String APP_SEC = "c2ed53a74eeefe3cf99fbd01d8c9c375";

    public static String appSign(Map<String, String> params) {
        // 为请求参数进行 APP 签名
        params.put("appkey", APP_KEY);
        // 按照 key 重排参数
        Map<String, String> sortedParams = new TreeMap<>(params);
        // 序列化参数
        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append('&');
            }

            try {
                queryBuilder
                        .append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return generateMD5(queryBuilder .append(APP_SEC).toString());
    }

    private static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
