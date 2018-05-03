package com.whalegoods.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.response.ResBody;
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
	public Integer getDeviceStatus(Map<String, Object> mapCdt) {
		return deviceMapper.getDeviceStatus(mapCdt);
	}

	@Override
	public ResBody getApk(Map<String, Object> mapCdt) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		resBody.setData(deviceMapper.getApk(mapCdt));
		return resBody;
	}

}
