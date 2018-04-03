package com.whalegoods.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.stereotype.Component;


@Component
public class MyServletContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("-------contextInitialized-----------");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("------------contextDestroyed-------------");
  }
}
