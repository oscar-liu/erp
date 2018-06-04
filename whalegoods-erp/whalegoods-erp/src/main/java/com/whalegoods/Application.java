package com.whalegoods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.whalegoods.exception.SystemException;
import com.whalegoods.job.JobRunner;;

/**
 * SpringBoot启动类
 * @author henrysun
 * 2018年4月26日 下午12:01:44
 */
@EnableWebMvc
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.whalegoods.mapper"}) 
@EnableScheduling
public class Application {
	
  public static void main(String[] args) throws SystemException {
	  SpringApplication.run(Application.class,args);
  }
  
  @Bean
  public JobRunner jobRunner(){
      return new JobRunner();
  }
}
