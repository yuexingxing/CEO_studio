package com.tajiang.ceo.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *  金额格式工具类
 * Created by Administrator on 2016/7/30.
 */
public class CashUtils {

    /**
     * 获取金额的字符串保留两位小数
     * @param cash
     * @return
     */
    public static String getCashWith2Point(BigDecimal cash){
        DecimalFormat format = new DecimalFormat("#.00");
        return format.format(cash);
    }


    /**
     * 检查金额的合法性，对金额补足$199.00
     * @param cash
     */
    public static String  CheckCashNumber(String cash) {
        String newCash = cash;
        if (!cash.equals("")) {
            switch (cash.charAt(0)) {
                case '.' :   //小数点开头
                    if (cash.length() > 1) {
                        if (cash.length() == 2) {
                            newCash = ("0" + cash + "0");  //小数点前面补0,最后一位补0
                        } else {
                            newCash = ("0" + cash);  //小数点前面补0
                        }
                    } else {
                        newCash = ("0" + cash + "00");  //小数点前后都补0
                    }
                    break;
                default:  //数字开头
                    if (cash.charAt(cash.length() - 1) == '.') { //最后一位为小数点  199.
                        newCash = (cash + "00");
                    } else {         //最后一位非小数点  199   199.0   199.00
                        int indexOfPoint = cash.indexOf('.');
                        int lastIndex = cash.length() - 1;

                        if (indexOfPoint == -1) {  //没有小数点，补足.00
                            newCash = (cash + ".00");
                        } else {
                            switch (lastIndex - indexOfPoint) {
                                case 0:
                                    newCash = (cash + "00");
                                    break;
                                case 1:
                                    newCash = (cash + "0");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    break;
            }
        }
        return  newCash;
    }

    /**
     * 分转为元，保留两位小数
     * @param fee
     * @param type 0不带标识，1-元
     * @return
     */
    public static String changeF2Y(int fee, int type){

        if(type == 0){
            return String.format("%.2f", fee/100.0);
        }else if(type == 1){
            return String.format("%.2f", (fee/100.0)) + "元";
        }else{
            return "¥" + String.format("%.2f", (fee/100.0));
        }

    }

}
