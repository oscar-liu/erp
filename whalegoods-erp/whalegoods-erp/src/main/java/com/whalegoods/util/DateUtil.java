package com.whalegoods.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;

import sun.tools.tree.ThisExpression;

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
	
	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}
	
	
	public static Date getFormatHms(String hms,Date date) throws SystemException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormat.parse(getCurrentYmd(date)+hms);
		} catch (ParseException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
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

	
	/** 
     * 判断时间是否在时间段内 
     * @param nowTime 
     * @param beginTime 
     * @param endTime 
     * @return 
     */  
  public static boolean belongTime(Date nowTime, Date beginTime, Date endTime) {  
    return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();  
  }
  
	/**
	 * 得到当前时间的年月日
	 * @author henrysun
	 * 2018年5月10日 上午11:00:43
	 */
	private static String getCurrentYmd(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date).substring(0,10);
	}
	
}
