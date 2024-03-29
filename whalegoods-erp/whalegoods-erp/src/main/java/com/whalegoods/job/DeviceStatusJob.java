package com.whalegoods.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.Device;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.SmsUtil;

/**
 * 根据上报时间查询离线的柜机
 * @author henrysun
 * 2018年6月3日 下午9:08:21
 */
public class DeviceStatusJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeviceService deviceService;
	
    public DeviceStatusJob() {}
  
    public void execute(JobExecutionContext context) {
		Long beforeTime = System.currentTimeMillis()-ConstSysParamValue.DEVICE_OFFLINE_TIME;
		List<Device> listOff=deviceService.selectListOfOffLine(beforeTime);
		logger.info("查询到的离线设备：{}",listOff.toString());
		if(listOff.size()>0){
			//任务名称必须和后台配置的一样，否则无法获取收件人列表
			String jobName="设备离线报警";
			//设备状态更新为离线
			deviceService.updateBatch(listOff);
			//发送内容
			StringBuffer sb=new StringBuffer();
			for (Device device : listOff) {
				sb.append(device.getShortName());
				sb.append(",");
			}
			String deviceNameArr = sb.toString().substring(0,sb.toString().length() - 1);
			//邮件模板
			String templateId="SMS_136860073";
			//邮件内容
			String content="{\"device_name\":\""+deviceNameArr+"\"}";
			//发送邮件
			SmsUtil.sendSms(jobName,templateId,content);
		}
    }

}
