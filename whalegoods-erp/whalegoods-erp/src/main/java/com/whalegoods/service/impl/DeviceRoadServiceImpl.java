package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;


@Service
public class DeviceRoadServiceImpl  implements DeviceRoadService {
	
	@Autowired
	DeviceRoadMapper deviceRoadMapper;
	
	@Override
	public List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String, Object> condition) {
		List<ResDeviceGoodsInfo> list=deviceRoadMapper.selectByIdOfJpAndSupp(condition);
		for (ResDeviceGoodsInfo resDeviceGoodsInfo : list) {
			//右连接查询，如果为空，需要做此转换
			resDeviceGoodsInfo.setType((byte) (resDeviceGoodsInfo.getType()!=null?1:2));
		}
		return list;
	}

	@Override
	public ResDeviceGoodsInfo selectByCondition(Map<String, Object> condition) {
		return deviceRoadMapper.selectByCondition(condition);
	}

	@Override
	public void updateDeviceRoad(DeviceRoad model){
		deviceRoadMapper.updateDeviceRoad(model);
	}

	@Override
	public void updateStockNoRepeat(DeviceRoad model) {
		deviceRoadMapper.updateStockNoRepeat(model);
	}

}
