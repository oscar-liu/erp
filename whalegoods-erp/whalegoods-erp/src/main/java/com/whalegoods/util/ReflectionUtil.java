package com.whalegoods.util;

import com.whalegoods.job.BaseJob;

/**
 * 反射工具类
 * @author henrysun
 * 2018年5月24日 上午11:05:43
 */
public class ReflectionUtil {

	public static BaseJob getClass(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }
}
