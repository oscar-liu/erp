package com.whalegoods.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.util.SmsUtil;


/**
 * 促销商品未上架通知
 * @author henrysun
 * 2018年8月8日 下午4:38:09
 */
public class MiddleAdsNotUpJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private GoodsAdsMiddleService adsMiddleService; 
	
    public MiddleAdsNotUpJob() {}
  
    public void execute(JobExecutionContext context) {
    	List<String> deviceList=adsMiddleService.selectNotUpDevice();
    	logger.info("查询到的未上架促销商品的设备：{}",deviceList.toString());
    	if(deviceList.size()>0){
			//任务名称必须和后台配置的一样，否则无法获取收件人列表
			String jobName="促销商品未上架通知";
			//短信模板
			String templateId="SMS_142010180";
			//短信内容
			StringBuffer sb=new StringBuffer();
			for (String shortName : deviceList) {
				sb.append(shortName);
				sb.append(",");
			}
			String shortNameArr = sb.toString().substring(0,sb.toString().length() - 1);
			String content="{\"short_name\":\""+shortNameArr+"\"}";
			//短信邮件
			SmsUtil.sendSms(jobName,templateId,content);
    	}
    }

}
