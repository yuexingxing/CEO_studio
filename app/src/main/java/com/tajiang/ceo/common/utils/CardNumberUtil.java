package com.tajiang.ceo.common.utils;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CardNumberUtil {

    /**
     * 清除字符串中间的空格
     * @param string
     * @return
     */
    public static String ClearBlank(String string){
        String newString = "";

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                newString += string.charAt(i);
            }
        }
        return newString;
    }

    /**
     * 格式化银行卡显示格式，隐藏中间9位数字
     * @param CardNumber
     * @return
     */
    public static String formatCardNumber(String CardNumber) {
        String number = "";
        int start = 0;
        int end = 0;
        if (CardNumber.length() > 9) {
            start = (CardNumber.length() - 9) / 2;
            end = CardNumber.length() - start - 9;
            if (start > 0) {
                number += CardNumber.substring(0, start);
            }
            number += "*********";
            number += CardNumber.substring((start + 9), CardNumber.length());
        } else {
            number = CardNumber;
        }
        return number;
    }
}
