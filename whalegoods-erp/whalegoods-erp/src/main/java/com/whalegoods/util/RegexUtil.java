package com.whalegoods.util;

import java.text.ParseException;
import java.util.Date;

import org.quartz.impl.triggers.CronTriggerImpl;

import com.whalegoods.exception.SystemException;

/**
 * 正则工具类
 * @author henrysun
 * 2018年5月22日 下午8:26:25
 */
public class RegexUtil {

	public static boolean isValidExpression(final String cronExpression) throws SystemException{
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
			trigger.setCronExpression(cronExpression);
			Date date = trigger.computeFirstFireTime(null);
	        return date != null && date.after(new Date());
		} catch (ParseException e) {
			return false;
		}
    }
}
