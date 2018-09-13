package com.ai.rai.interests.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Title: 手机号码工具类 <br>
 * Description: <br>
 * Date: 2016年9月20日 <br>
 * Copyright (c) 2016 AILK <br>
 * 
 * @author liangyi
 */
public class PhoneUtil {
    
    /**联通手机号前三位*/
    private static final String prefixUnicom[] = {"130", "131", "132", "155", "156", "185", "186", "145", "176"};

    private static final String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
    private static Pattern p = Pattern.compile(regExp);  
    
    /**
     * 判断是否为联通手机号
     * @param phoneNum 手机号码
     * @return  true：是联通号码；false：非联通号码
     * @author liangyi
     */
    public static boolean isChinaUnicomNumber(String phoneNum){
        if (phoneNum==null||phoneNum.length()!=11) {
            return false;
        }
        //判断是否为纯数字
        for (int i=0;i<phoneNum.length();i++) {
			char c=phoneNum.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
        boolean flag = false;
        for (String aPrefixUnicom : prefixUnicom) {
            if (phoneNum.startsWith(aPrefixUnicom)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    /** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 
     * 此方法中前三位格式有： 
     * 13+任意数 
     * 15+除4的任意数 
     * 18+除1和4的任意数 
     * 17+除9的任意数 
     * 147 
     */  
    public static boolean isChinesePhonenumber(String phonenumber){  
        Matcher m = p.matcher(phonenumber);  
        return m.matches();  
    }  
    
}
