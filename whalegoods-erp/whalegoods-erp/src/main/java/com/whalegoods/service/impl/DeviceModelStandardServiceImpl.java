package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceModelStandard;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceModelStandardMapper;
import com.whalegoods.service.DeviceModelStandardService;

@Service
public class DeviceModelStandardServiceImpl extends BaseServiceImpl<DeviceModelStandard,String> implements DeviceModelStandardService {
	
	  @Autowired
	  DeviceModelStandardMapper deviceModelStandardMapper;

	  @Override
	  public BaseMapper<DeviceModelStandard, String> getMapper() {
	    return deviceModelStandardMapper;
	  }

	@Override
	public int deleteByDeviceModelId(String id) {
		return deviceModelStandardMapper.deleteByDeviceModelId(id);
	}
	  
}
