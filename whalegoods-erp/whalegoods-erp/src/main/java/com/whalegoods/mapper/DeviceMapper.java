package com.whalegoods.mapper;


import java.util.List;

import com.whalegoods.entity.Device;

public interface DeviceMapper extends BaseMapper<Device,String> {

	List<Device> selectListOfOffLine(Long beforeTime);
	
	int updateBatch(List<Device> listDevice);
}