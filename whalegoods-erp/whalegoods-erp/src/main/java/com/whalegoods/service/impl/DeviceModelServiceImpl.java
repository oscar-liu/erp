package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceModel;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceModelMapper;
import com.whalegoods.service.DeviceModelService;

@Service
public class DeviceModelServiceImpl extends BaseServiceImpl<DeviceModel,String> implements DeviceModelService {
	
	  @Autowired
	  DeviceModelMapper deviceModelMapper;

	  @Override
	  public BaseMapper<DeviceModel, String> getMapper() {
	    return deviceModelMapper;
	  }
	  
}
