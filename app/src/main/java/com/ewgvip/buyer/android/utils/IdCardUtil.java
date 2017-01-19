package com.ewgvip.buyer.android.utils;

/**
 * Created by Administrator on 2016/7/6.
 */
public  class IdCardUtil {

    static   int[] weight={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};    //十七位数字本体码权重
    static  char[] validate={ '1','0','X','9','8','7','6','5','4','3','2'};    //mod11,对应校验码字符值

    //检查身份证号码是否正确
    public static boolean checkCard(String id18) {
        int sum = 0;
        int mode = 0;
        String id17=id18.substring(0,17);
        for (int i = 0; i < id17.length(); i++) {
            sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
        }
        mode = sum % 11;
        char validateCode=id18.charAt(17);
        char code = validate[mode];
        if (code==validateCode){
            return true;
        }
        return false;
    }
}