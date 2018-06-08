package com.whalegoods.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.whalegoods.entity.response.ResJobEmail;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.SysUserService;

/**
 * 短信工具类
 * @author henrysun
 * 2018年6月7日 下午5:24:27
 */
@Component
public class SmsUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SmsUtil.class);
	
	private static SysUserService sysUserService;
	
    @Autowired
    private SysUserService sysUserService2;
    
    @PostConstruct
    public void beforeInit() {
    	sysUserService = sysUserService2;
    }
	
    /**
     * 发送短信
     * jobName 任务名称  
     * templateId 短信模板
     * content  短信内容
     * @author henrysun
     * 2018年6月9日 上午12:01:07
     */
	public static void sendSms(String jobName,String templateId,String content){
		//查询收件人列表
		List<ResJobEmail> contrats=sysUserService.getEmailArr(jobName);
		if(contrats.size()>0&&contrats.get(0).getSwitchStatus()==1){
			StringBuffer sb2=new StringBuffer();
			for (ResJobEmail item : contrats) {
				sb2.append(item.getPhone());
				sb2.append(",");
			}
			String phongArr = sb2.toString().substring(0,sb2.toString().length() - 1);
			logger.info("查询到的发送短信的手机号：{}",phongArr);
			try {
				sendSmsRequest(phongArr,templateId,content);
			} catch (SystemException e) {
				logger.error("发送短信失败：{}",e.getMessage());
			}
		}
	}
	
	/**
	 * 批量发送短信
	 * @author henrysun
	 * 2018年6月7日 下午5:25:39
	 * @throws SystemException 
	 */
	private static void sendSmsRequest(String phongArr,String templateId,String content) throws SystemException{
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
		 request.setTemplateCode(templateId);
		 request.setTemplateParam(content);
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
