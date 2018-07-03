package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceExLog;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceExlogMapper;
import com.whalegoods.service.DeviceExlogService;

@Service
public class DeviceExlogServiceImpl extends BaseServiceImpl<DeviceExLog,String> implements DeviceExlogService {
	
	  @Autowired
	  DeviceExlogMapper deviceExlogMapper;

	  @Override
	  public BaseMapper<DeviceExLog, String> getMapper() {
	    return deviceExlogMapper;
	  }
}
