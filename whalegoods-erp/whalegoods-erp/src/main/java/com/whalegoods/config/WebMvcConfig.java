package com.whalegoods.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * WebMvc配置
 * @author henry-sun
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

	public String getLocation() {
		return env.getProperty("spring.http.multipart.location");
	}
	
	/**
	 * 使用fastjson注册bean
	 * @return
	 */
	@Bean
	    public HttpMessageConverters fastJsonHttpMessageConverters() {
	        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
	        FastJsonConfig config=new FastJsonConfig();
	        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
	        converter.setFastJsonConfig(config);
	        return new HttpMessageConverters(converter);
	    }

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/plugin/**","/static/**").addResourceLocations("classpath:/plugin/","classpath:/static/");
	        registry.addResourceHandler("/ftl/**").addResourceLocations("classpath:/ftl/");
	        registry.addResourceHandler("/v1/ex_log/**").addResourceLocations("file:"+getLocation());
	    }
	    
}
