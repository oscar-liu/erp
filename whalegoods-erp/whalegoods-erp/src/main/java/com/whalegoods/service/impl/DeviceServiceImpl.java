package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.Device;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceMapper;
import com.whalegoods.service.DeviceService;


@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device,String>  implements DeviceService {
	
	@Autowired
	DeviceMapper deviceMapper;
	
	@Override
	public BaseMapper<Device, String> getMapper() {
		return deviceMapper;
	}

	@Override
	public List<Device> selectListOfOffLine(Long beforeTime) {
		return deviceMapper.selectListOfOffLine(beforeTime);
	}

	@Override
	public int updateBatch(List<Device> listDevice) {
		return deviceMapper.updateBatch(listDevice);
	}

}
