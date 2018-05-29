package com.whalegoods.service;

import com.whalegoods.entity.DeviceModelStandard;

public interface DeviceModelStandardService extends BaseService<DeviceModelStandard,String> {	

	int deleteByDeviceModelId(String id);
}
