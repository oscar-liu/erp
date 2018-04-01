package com.whalegoods.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.whalegoods.util.ApplicationContextUtil;

/**
 * 应用上下文配置
 * @author chencong
 * 2018年3月30日 下午7:52:35
 */
public class ApplicationContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;
  
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationContextUtil.applicationContext=applicationContext;
  }

  public static ApplicationContext getContext(){
    return applicationContext;
  }

  public static Object getBean(String arg){
    return applicationContext.getBean(arg);
  }
}
