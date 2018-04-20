package com.whalegoods.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 * @author henry-sun
 *
 */
public class DateUtil {
	
	/**
	 * 日期时间格式化对象
	 */
	private static DateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

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
	
	/**
	 * 格式化输出 默认格式为 "yyyy-MM-dd HH:mm"
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		if (date == null)
			return "";
		return dateTimeFormat.format(date);
	}
	
}
