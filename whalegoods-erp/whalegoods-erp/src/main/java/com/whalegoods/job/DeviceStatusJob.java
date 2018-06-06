package com.whalegoods.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.Device;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.EmailService;
import com.whalegoods.service.SysUserService;

/**
 * 根据上报时间查询离线的柜机
 * @author henrysun
 * 2018年6月3日 下午9:08:21
 */
public class DeviceStatusJob implements BaseJob{
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SysUserService sysUserService;
	
    public DeviceStatusJob() {}
  
    public void execute(JobExecutionContext context) {
		Long beforeTime = System.currentTimeMillis()-ConstSysParamValue.DEVICE_OFFLINE_TIME;
		List<Device> listOff=deviceService.selectListOfOffLine(beforeTime);
		if(listOff.size()>0){
			//邮件主题必须和任务名称一致，否则会导致收件人列表为空
			String subject="设备下线报警";
			//邮件内容
			StringBuffer sb=new StringBuffer();
			for (Device device : listOff) {
				sb.append("<strong>");
				sb.append(device.getShortName());
				sb.append("</strong>");
				sb.append("的设备已下线");
				sb.append("\n");
			}
			//查询收件人列表
			String [] toList=sysUserService.getEmailArr(subject);
			if(toList.length>0){
				emailService.sendSimpleMail(toList,subject,sb.toString());
			}
		}
    }

}
