package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.DeviceModelStandard;

public interface DeviceModelStandardService extends BaseService<DeviceModelStandard,String> {	

	int deleteByDeviceModelId(String id);
	
	int insertBatch(List<DeviceModelStandard> list);
}
