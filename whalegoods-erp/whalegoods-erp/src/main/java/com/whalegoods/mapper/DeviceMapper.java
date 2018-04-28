package com.whalegoods.mapper;


import com.whalegoods.entity.Device;

import java.util.Map;


public interface DeviceMapper extends BaseMapper<Device,String> {

	/**
	 * 获取最新客户端APK
	 * @author henrysun
	 * 2018年4月28日 下午2:46:35
	 */
    Map<String,Object> getApk(Map<String, Object> condition);

	int getOperateStatus(Map<String, Object> condition);
	
	int updateClient(Map<String,Object> condition);
	
	int updateDeviceStatus(Map<String,Object> condition);
}