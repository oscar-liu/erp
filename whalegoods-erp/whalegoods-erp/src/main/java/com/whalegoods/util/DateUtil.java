package com.whalegoods.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;


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
			"yyyy-MM-dd HH:mm:ss");

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
			return dateFormat.parse(getCurrentYmd(date)+" "+hms);
		} catch (ParseException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 格式化输出 默认格式为 "yyyy-MM-dd HH:mm:ss"
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		if (date == null)
			return "";
		return dateTimeFormat.format(date);
	}
	
	/**
	 * string转date
	 * @author henrysun
	 * 2018年5月15日 下午10:59:01
	 * @throws SystemException 
	 */
	public static Date stringToDate(String date) throws SystemException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
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
   * 时间戳转Date
   * @author henrysun
   * 2018年6月11日 下午12:57:48
 * @throws SystemException 
   */
  public static Date timestampToDate(Long timestamp) throws SystemException{
	  try {
		return dateTimeFormat.parse(dateTimeFormat.format(timestamp)) ;
	} catch (ParseException e) {
		throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	}
  }
  
  /**
   * 计算两个日期之间差多少个月
   * @author henrysun
   * 2018年5月17日 上午9:56:45
 * @throws SystemException 
   */
  public static int countMonths(String date1,String date2,String pattern) throws SystemException{
      SimpleDateFormat sdf=new SimpleDateFormat(pattern);
      Calendar c1=Calendar.getInstance();
      Calendar c2=Calendar.getInstance();
      try {
		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));
	} catch (ParseException e) {
		throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
	}
      int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
      //开始日期若小月结束日期
      if(year<0){
          year=-year;
          return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
      }
      return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
  }
  
  /**
   * 计算两个日期之间的月份集合
   * @author henrysun
   * 2018年5月17日 上午10:08:55
   */
  public static List<String> getMonthBetween(String startDate, String endDate,String pattern) throws SystemException {
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);//格式化为年月
	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();
	    try {
			min.setTime(sdf.parse(startDate));
		} catch (ParseException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
	    try {
			max.setTime(sdf.parse(endDate));
		} catch (ParseException e) {
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }
	    return result;
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
	
	@SuppressWarnings("static-access")
	public static  String getMoveDay(Date date){
	        Calendar calendar = new GregorianCalendar();
	        calendar.setTime(date);
	        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
	        date=calendar.getTime(); //这个时间就是日期往后推一天的结果        
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return formatter.format(date); 
	}
	
	/**
	 * 得到前后移动一定时间后的时间戳
	 * @author henrysun
	 * 2018年6月3日 下午9:14:21
	 */
	public static  Long getMoveTimestamp(Date date,Integer moveType,Integer moveTime){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(moveType,moveTime);//整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        return date.getTime();
}
	
}
