package com.whalegoods.util;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.service.DeviceService;

/**
 * 公共校验类
 * @author henrysun
 * 2018年6月3日 下午6:41:14
 */
@Component
public class CommonValidateUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(CommonValidateUtil.class);
	
	private static DeviceService deviceService;
	
    @Autowired
    private DeviceService deviceService2;
    
    @PostConstruct
    public void beforeInit() {
    	deviceService = deviceService2;
    }

	/**
	 * 根据鲸品设备编号和供应商设备编号判断是否是否存在
	 * @author henrysun
	 * 2018年6月3日 下午6:43:01
	 */
	public static void validateDeviceExist(String deviceIdJp,String deviceIdSupp){
		Device objCdt=new Device();
		objCdt.setDeviceIdJp(deviceIdJp);
		objCdt.setDeviceIdSupp(deviceIdSupp);
		if(deviceService.selectByObjCdt(objCdt)==null){
			logger.info("设备不存在");
			throw new BizServiceException(ConstApiResCode.DEVICE_NOT_EXIST);
		}
	}
}
