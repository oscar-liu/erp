package com.whalegoods.service;

/**
 * 邮件处理Service接口层
 * @author henry-sun
 *
 */
public interface EmailService {

	void sendSimpleMail(String to, String subject, String content);
}
