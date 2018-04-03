package com.whalegoods.config.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * freemarker结合shiro配置
 * @author henry-sun
 *
 */
@Component
public class FreemarkerShiroConfig implements InitializingBean {

  @Autowired
  private freemarker.template.Configuration configuration;

  @Override
  public void afterPropertiesSet() throws Exception {
    configuration.setSharedVariable("shiro", new ShiroTags());
  }
}
