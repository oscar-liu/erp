package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.Device;


public interface DeviceService extends BaseService<Device,String> {

	List<Device> selectListOfOffLine(Long beforeTime);
	
	int updateBatch(List<Device> listDevice);
	
	List<String> selectOosDevice();
}
