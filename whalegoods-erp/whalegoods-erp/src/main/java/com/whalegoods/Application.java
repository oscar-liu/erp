package com.whalegoods;

import java.util.Arrays;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启动类
 * @author chencong
 *
 */
@EnableWebMvc
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.len.mapper"})
public class Application {

  public static void main(String[] args) {
    ApplicationContext applicationContext=SpringApplication.run(Application.class,args);
    String[] names = applicationContext.getBeanDefinitionNames();
    Arrays.asList(names).forEach(name -> System.out.println(name));//1.8 forEach循环
  }



}
