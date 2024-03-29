package com.whalegoods.config.freemarker;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * freemarker配置
 * @author henrysun
 * 2018年4月26日 下午3:36:14
 */
@Configuration
public class FreeMarkerConfig{

  @Bean
  public ViewResolver viewResolver() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setCache(false);
    resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
    //配置上下文根目录，在模版页中使用re开头获取路径
    resolver.setRequestContextAttribute("re");
    resolver.setExposeRequestAttributes(true);
    resolver.setExposeSessionAttributes(true);
    resolver.setSuffix(".ftl");
    resolver.setContentType("text/html; charset=UTF-8");
    return resolver;
  }


  @Bean
  public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
    FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
    factory.setTemplateLoaderPath("classpath:/ftl/");
    factory.setDefaultEncoding("UTF-8");
    factory.setPreferFileSystemAccess(false);
    FreeMarkerConfigurer result = new FreeMarkerConfigurer();
    freemarker.template.Configuration configuration = factory.createConfiguration();
    configuration.setClassicCompatible(true);
    result.setConfiguration(configuration);
    Properties settings = new Properties();
    settings.put("template_update_delay", "0");
    settings.put("default_encoding", "UTF-8");
    settings.put("number_format", "0.######");
    settings.put("classic_compatible", true);
    settings.put("template_exception_handler", "ignore");
    result.setFreemarkerSettings(settings);
    return result;
  }

}