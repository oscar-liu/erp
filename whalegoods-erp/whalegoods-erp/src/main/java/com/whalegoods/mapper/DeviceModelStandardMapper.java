package com.whalegoods.mapper;

import com.whalegoods.entity.DeviceModelStandard;

public interface DeviceModelStandardMapper extends BaseMapper<DeviceModelStandard,String> {
	
    int deleteByDeviceModelId(String id);
    
}