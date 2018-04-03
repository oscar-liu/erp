package com.whalegoods.config.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * log配置
 * @author henry-sun
 *
 */
@Configuration
public class LogConfig {

  @Bean(name = "logAspect")
  public LogAspect getLogAspect(){
    return new LogAspect();
  }

}
