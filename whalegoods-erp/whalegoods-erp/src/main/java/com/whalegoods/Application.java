package com.whalegoods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
@EnableAsync
public class Application {
	
  public static void main(String[] args) throws SystemException {
	  SpringApplication.run(Application.class,args);
  }
  
  /**
   * 给com.whalegoods.temp包下的类使用
   * @author henrysun
   * 2018年8月22日 下午5:23:33
   */
 /* @Bean(name = "multipartResolver")
  public MultipartResolver multipartResolver(){
   CommonsMultipartResolver resolver = new CommonsMultipartResolver();
   resolver.setDefaultEncoding("UTF-8");
   resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
   resolver.setMaxInMemorySize(40960);
   resolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
   return resolver;
} */
  
  @Bean
  public JobRunner jobRunner(){
      return new JobRunner();
  }
}
