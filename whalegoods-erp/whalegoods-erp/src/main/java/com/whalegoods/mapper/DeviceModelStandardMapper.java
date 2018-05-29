package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.DeviceModelStandard;

public interface DeviceModelStandardMapper extends BaseMapper<DeviceModelStandard,String> {
	
    int deleteByDeviceModelId(String id);
    
    int insertBatch(List<DeviceModelStandard> list);
}