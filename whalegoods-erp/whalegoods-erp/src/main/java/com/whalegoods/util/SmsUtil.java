package com.whalegoods.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.exception.SystemException;

/**
 * 短信工具类
 * @author henrysun
 * 2018年6月7日 下午5:24:27
 */
public class SmsUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SmsUtil.class);
	
	/**
	 * 批量发送短信
	 * @author henrysun
	 * 2018年6月7日 下午5:25:39
	 * @throws SystemException 
	 */
	public static void SendSmsRequest(String phongArr,String deviceNameArr) throws SystemException{
		System.setProperty("sun.net.client.defaultConnectTimeout","10000");
		System.setProperty("sun.net.client.defaultReadTimeout","10000");
		final String product = "Dysmsapi";
		final String domain = "dysmsapi.aliyuncs.com";
		//替换成你的AK
		final String accessKeyId = "LTAI213BPH0yvjUE";
		final String accessKeySecret = "H5Uf5YNrGST3QyE4XYvzQuSKhJALoY";
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			logger.error("执行SendSmsRequest>>addEndpoint异常：{}",e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 SendSmsRequest request = new SendSmsRequest();
		 request.setMethod(MethodType.POST);
		 request.setPhoneNumbers(phongArr);
		 request.setSignName("鲸品智能");
		 request.setTemplateCode("SMS_136860073");
		 request.setTemplateParam("{\"device_name\":\""+deviceNameArr+"\"}");
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ServerException e) {
			logger.error("执行SendSmsRequest>>getAcsResponse异常：{}",e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} catch (ClientException e) {
			logger.error("执行SendSmsRequest>>getAcsResponse异常：{}",e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			logger.error("短信发送成功");
		}
		else{
			logger.error("短信发送失败，原因：{}",sendSmsResponse.getMessage());
		}
	}
}
