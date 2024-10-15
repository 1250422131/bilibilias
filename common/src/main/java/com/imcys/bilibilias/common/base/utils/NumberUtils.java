package com.imcys.bilibilias.common.base.utils;

/**
 * @author:imcys
 * @create: 2022-11-14 22:54
 * @Description:
 */
public class NumberUtils {

    public static String digitalConversion(int num) {
        String originallyNum = num + "";
        String result;
        if (num >= 10000) {
            int count = lengthNum(num);
            switch (count) {
                case 5 -> {
                    result = originallyNum.charAt(0) + "." + originallyNum.charAt(1) + "万";
                    return result;
                }
                case 6 -> {
                    result = originallyNum.substring(0, 2) + "." + originallyNum.charAt(2) + "万";
                    return result;
                }
                case 7 -> {
                    result = originallyNum.substring(0, 3) + "." + originallyNum.charAt(3) + "万";
                    return result;
                }
                default -> {
                    result = originallyNum.substring(0, 4) + "." + originallyNum.charAt(4) + "万";
                    return result;
                }
            }
        } else {
            return num + "";
        }
    }


    public static int lengthNum(int num) {
        int count = 0; //计数
        while (num >= 1) {
            num /= 10;
            count++;
        }
        return count;
    }
}
