package com.whalegoods.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.service.DeviceService;
import com.whalegoods.util.SmsUtil;


/**
 * 库存不足通知
 * @author henrysun
 * 2018年6月8日 下午4:38:09
 */
public class PathStockJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeviceService deviceService;
	
    public PathStockJob() {}
  
    public void execute(JobExecutionContext context) {
    	List<String> oosDeviceList=deviceService.selectOosDevice();
    	logger.info("查询到的缺货设备：{}",oosDeviceList.toString());
    	if(oosDeviceList.size()>0){
			//任务名称必须和后台配置的一样，否则无法获取收件人列表
			String jobName="库存不足通知";
			//邮件模板
			String templateId="SMS_137665800";
			//邮件内容
			String content="{\"deviceCount\":\""+oosDeviceList.size()+"\"}";
			//发送邮件
			SmsUtil.sendSms(jobName,templateId,content);
    	}
    }

}
