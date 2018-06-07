package com.whalegoods.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.whalegoods.service.EmailService;
import com.whalegoods.util.EnvironmentUtil;


@Service
public class EmailServiceImpl  implements EmailService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private JavaMailSender mailSender;
    
	@Autowired
	private TaskExecutor taskExecutor;

	
	@Override
	public void sendSimpleMail(String to,String subject,String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			 MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	         helper.setFrom(EnvironmentUtil.getEmailFrom());
	         helper.setTo(to); //收件人
	         helper.setSubject(subject);
	         helper.setText(content,true);//设置为TRUE则可以使用Html标记
		} catch (MessagingException e) {
			logger.error("执行sendSimpleMail异常：{}",e.getMessage());
		}
         addSendMailTask(message);
	}
	
	/**
	 * 多线程发送邮件
	 * @author henrysun
	 * 2018年6月6日 上午11:19:24
	 */
    private  void addSendMailTask(final MimeMessage message){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
            	mailSender.send(message);
            }
        });
    }

	
}
