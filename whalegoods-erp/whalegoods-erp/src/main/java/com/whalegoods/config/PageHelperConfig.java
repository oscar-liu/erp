package com.whalegoods.config;

import com.github.pagehelper.PageHelper;
import java.util.Properties;

/**
 * pagehelper配置
 * 此方案可替代 application.yml中 pagehelper配置
 * @author henry-sun
 *
 */
//@Configuration
public class PageHelperConfig {


  //@Bean
  public PageHelper getPageHelper(){
    PageHelper pageHelper=new PageHelper();
    Properties properties=new Properties();
    properties.setProperty("helperDialect","mysql");
    properties.setProperty("reasonable","true");
    properties.setProperty("supportMethodsArguments","true");
    properties.setProperty("params","count=countSql");
    pageHelper.setProperties(properties);
    return pageHelper;
  }

}
