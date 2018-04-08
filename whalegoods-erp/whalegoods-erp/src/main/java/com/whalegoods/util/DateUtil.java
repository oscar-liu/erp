package com.whalegoods.util;

import java.util.Date;

/**
 * 日期处理工具类
 * @author henry-sun
 *
 */
public class DateUtil {

	/**
	 * 返回两个时间相差的秒数
	 * @param startTime
	 * @param endTime
	 * @return f
	 */
	public static final int secsSub(Date startTime, Date endTime) {
		int date = (int) (endTime.getTime() / 1000 - startTime.getTime()
				/1000);
		return date;
	}
}
