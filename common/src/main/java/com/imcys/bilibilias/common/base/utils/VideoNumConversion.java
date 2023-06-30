package com.imcys.bilibilias.common.base.utils;


/**
 * av转bv类
 * thanks mcfx - https://www.zhihu.com/question/381784377/answer/1099438784
 * https://github.com/BakaJzon/bv2av-java/blob/master/src/bakajzon/bv2av/Offline.java
 *
 * @author BakaJzon
 */
public class VideoNumConversion {

    private VideoNumConversion() {
    }

    public static int toAvidOffline(String bvid) {
//		if(!bvid.startsWith("BV")) throw new IllegalArgumentException("bvid must start with \"BV\"");
        long r = 0L, m = 1L;
        char[] bvchars = bvid.toCharArray();
        for (int i = 0; i < 6; i++, m *= 58)
            r += Data.TR[bvchars[Data.S[i]]] * m;
        return (int) ((r - Data.ADD) ^ Data.XOR);
    }

    public static String toBvidOffline(int avid) {
//		if(aid < 0) throw new IllegalArgumentException("aid must greater than 0");
        long x = (avid ^ Data.XOR) + Data.ADD, m = 1L;
        char[] r = "BV1  4 1 7  ".toCharArray();
        for (int i = 0; i < 6; i++, m *= 58)
            r[Data.S[i]] = Data.TABLE[(int) (x / m % 58)];
        return String.valueOf(r);
    }

    static class Data {
        static final char[] TABLE, TR;

        static final byte[] S = new byte[]{11, 10, 3, 8, 4, 6};

        static final long XOR = 177451812, ADD = 8728348608L;

        static {
            TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF".toCharArray();
            TR = new char[124];
            for (char i = 0; i < 58; i++)
                TR[TABLE[i]] = i;
        }
    }

}