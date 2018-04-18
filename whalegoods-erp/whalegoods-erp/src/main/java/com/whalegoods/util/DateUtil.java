package com.whalegoods.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期处理工具类
 * @author henry-sun
 *
 */
public class DateUtil {

	/**
	 * 返回当前“年月”字符串，例如“2018_04”
	 * @author chencong
	 * 2018年4月18日 下午4:30:57
	 */
	public static String getCurrentMonth() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(Calendar.getInstance().getTime());
	}
	
}
