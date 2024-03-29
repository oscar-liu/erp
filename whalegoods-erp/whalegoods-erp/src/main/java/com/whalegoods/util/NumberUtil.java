package com.whalegoods.util;

/**
 * 数值相关工具类
 * @author henrysun
 * 2018年5月5日 上午10:12:12
 */
public class NumberUtil {

	/**
	 * 元转分
	 * @author henrysun
	 * 2018年5月5日 上午10:12:24
	 */
    public static String changeY2F(Double amount){    
        String currency =  String.valueOf(amount).replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额    
        int index = currency.indexOf(".");    
        int length = currency.length();    
        Long amLong = 0l;    
        if(index == -1){    
            amLong = Long.valueOf(currency+"00");    
        }else if(length - index >= 3){    
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
        }else if(length - index == 2){    
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
        }else{    
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
        }    
        return amLong.toString();
    }    
}
