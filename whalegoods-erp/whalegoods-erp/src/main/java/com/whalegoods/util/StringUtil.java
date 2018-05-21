package com.whalegoods.util;

import java.util.Random;
import java.util.UUID;

import com.whalegoods.constant.ConstSysParamName;

/**
 * 字符串工具类
 * @author henrysun
 * 2018年5月3日 下午3:55:18
 */
public class StringUtil {

	/**
	 * 生成固定长度的随机字符串（字母数字）
	 * @param length 传入的字符串的长度
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int r = (int) (Math.random() * 3);
			int rn1 = (int) (48 + Math.random() * 10);
			int rn2 = (int) (65 + Math.random() * 26);
			int rn3 = (int) (97 + Math.random() * 26);
			switch (r) {
			case 0:
				builder.append((char) rn1);
				break;
			case 1:
				builder.append((char) rn2);
				break;
			case 2:
				builder.append((char) rn3);
				break;
			}
		}
		return builder.toString();
	}
	
	/**
	 * 生成系统订单号
	 * @author chencong
	 * 2018年4月10日 下午1:38:41
	 */
	public static String getOrderId(){
		StringBuffer sb=new StringBuffer();
		sb.append(ConstSysParamName.ORDER_ID_PREFIX);
		sb.append(String.valueOf(System.currentTimeMillis()));
		sb.append(randomString(17));
		return sb.toString();
	}
	
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replace(ConstSysParamName.GANG,"");
	}
	
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}
	
	public static int getNumberRadom(){
		Random random=new Random();
		return random.nextInt(1000000);
	}
}
