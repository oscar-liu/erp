package com.whalegoods.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * profile参数工具类
 * @author henrysun
 * 2018年6月4日 下午2:35:55
 */
@Component
public class EnvironmentUtil {

	private static Environment env;
	
    @Autowired
    private Environment env2;
    
    @PostConstruct
    public void beforeInit() {
    	env = env2;
    }
    
    /**
     * 获取当前环境文件上传路径
     * @author henrysun
     * 2018年6月4日 下午2:38:19
     */
	public static String getUploadPath() {
		return env.getProperty("spring.http.multipart.location");
	}
	
	/**
	 * 获取当前环境文件访问地址
	 * @author henrysun
	 * 2018年6月4日 下午2:38:38
	 */
	public static String getFileUrl() {
		return env.getProperty("spring.http.multipart.addr");
	}
	
	/**
	 * 获取邮件发送方地址
	 * @author henrysun
	 * 2018年6月4日 下午2:38:38
	 */
	public static String getEmailFrom() {
		return env.getProperty("spring.mail.username");
	}
}
