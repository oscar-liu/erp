package com.whalegoods.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮件工具类
 * @author henrysun
 * 2018年6月3日 下午11:26:09
 */
@Component
public class EmailUtil {
	
	private static JavaMailSender mailSender;
	
    @Autowired
    private JavaMailSender mailSender2;
    
    @PostConstruct
    public void beforeInit() {
    	mailSender = mailSender2;
    }

	public static void sendSimpleMail(String to,String subject,String content){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(EnvironmentUtil.getEmailFrom());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}
}
