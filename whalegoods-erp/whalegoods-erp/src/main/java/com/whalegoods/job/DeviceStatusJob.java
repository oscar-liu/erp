package com.whalegoods.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.Device;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.EmailUtil;

/**
 * 根据上报时间查询离线的柜机
 * @author henrysun
 * 2018年6月3日 下午9:08:21
 */
public class DeviceStatusJob implements BaseJob{
	
	@Autowired
	private DeviceService deviceService;
	
    public DeviceStatusJob() {}
  
    public void execute(JobExecutionContext context) {
		Long beforeTime = System.currentTimeMillis() - ConstSysParamValue.DEVICE_OFFLINE_TIME;
		List<Device> listOff=deviceService.selectListOfOffLine(beforeTime);
		if(listOff.size()>0){
			String from="chencong@whalegoods.com";
			for (Device device : listOff) {
				EmailUtil.sendSimpleMail("chencong",from,"测试邮件","测试邮件的内容");
			}
		}
    }

}
