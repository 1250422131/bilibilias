package com.imcys.bilibilias.home;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class VerificationUtils {
    // TODO 逻辑算法设计 哔哩哔哩：萌新杰少
    static String[] POWER_LIST = new String[]{"²", "³", "⁴"};

    /**
     * 导函数代入值验证 目前只写了平方到4次方，再大用户就不好算了
     * @return object 题目和答案的JSON
     */
    public static JSONObject derivatives() {
        Random rand = new Random();
        JSONObject object = new JSONObject();
        //次方数
        int multiplier = rand.nextInt(3);
        int multiplier_multiplicand = 0;
        int cycles = 0;
        //乘数
        int multiplicand = rand.nextInt(10 - 1) + 1;
        int value;
        int derivativeValue = 0;
        int derivatives = rand.nextInt(10 - 1) + 1;
        if (multiplier == 0) {
            multiplier_multiplicand = 2;
        } else if (multiplier == 1) {
            //循环一次是平方
            cycles = 1;
            multiplier_multiplicand = 3;
        } else if (multiplier == 2) {
            //平方的基础上再乘以这个数
            cycles = 2;
            multiplier_multiplicand = 4;
        }
        for (int i = 0; i < cycles; i++) {
            //平方操作
            if (derivativeValue == 0) {
                derivativeValue = derivatives * derivatives;
            } else {
                derivativeValue = derivativeValue * derivatives;
            }
        }
        if (cycles == 0) {
            derivativeValue = derivatives;
        }
        int multiplicandValue = multiplier_multiplicand * multiplicand;
        derivativeValue = multiplicandValue * derivativeValue;
        value = derivativeValue;
        try {
            object.put("problem", "已知函数f(x)=" + multiplicand + "X" + POWER_LIST[multiplier] + "，求它的导函数f'(" + derivatives + ")时的值。");
            object.put("answer", "" + value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 甲烷同系物验证
     * @return object 题目和答案的JSON
     */
    public static JSONObject organicCompound() {
        Random rand = new Random();
        JSONObject object = new JSONObject();
        int carbonNumber = rand.nextInt(6 - 1) + 1;
        String structural = "";
        for (int i = 0; i < carbonNumber; i++) {
            if (i == carbonNumber - 1) {
                structural = structural + "C";
            } else {
                structural = structural + "C-";
            }
        }
        String value = null;
        //减去两端的碳剩下的每个会连2个氢
        if (carbonNumber == 1) {
            value = "CH4";
        } else if (carbonNumber == 2) {
            value = "C2H6";
        } else {
            int value1 = carbonNumber - 2;
            int value2 = 2 * 3;
            value1 = value1 * 2;
            value1 = value1 + value2;
            value = "C" + carbonNumber + "H" + value1;
        }
        try {
            object.put("problem", "已知某甲烷的同系物未补全的结构式为\n" + structural + "\n请补全碳链的氢，并且写出分子式");
            object.put("answer", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
