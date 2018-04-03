package com.whalegoods.config;

import java.nio.charset.Charset;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * WebMvc配置
 * @author henry-sun
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	 @Bean
	    public HttpMessageConverter<String> responseBodyConverter() {
	        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
	        return converter;
	    }

	    @Override
	    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	        super.configureMessageConverters(converters);
	        converters.add(responseBodyConverter());
	    }

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/plugin/**","/static/**")
	            .addResourceLocations("classpath:/plugin/","classpath:/static/");
	        registry.addResourceHandler("/ftl/**").addResourceLocations("classpath:/ftl/");
	    }
}
