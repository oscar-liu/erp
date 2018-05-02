package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;

/**
 * 货道商品信息业务逻辑实现类
 * @author henrysun
 * 2018年5月2日 下午2:33:07
 */
@Service
public class DeviceRoadServiceImpl extends BaseServiceImpl<DeviceRoad,String> implements DeviceRoadService {
	
	@Autowired
	DeviceRoadMapper deviceRoadMapper;
	
	@Override
	public BaseMapper<DeviceRoad, String> getMapper() {
		return deviceRoadMapper;
	}
	
	@Override
	public List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String, Object> condition) {
		List<ResDeviceGoodsInfo> list=deviceRoadMapper.selectByIdOfJpAndSupp(condition);
		for (ResDeviceGoodsInfo resDeviceGoodsInfo : list) {
			//右连接查询，如果为空，需要做此转换
			resDeviceGoodsInfo.setSaleType((byte) (resDeviceGoodsInfo.getSaleType()!=null?1:2));
		}
		return list;
	}

	@Override
	public ResDeviceGoodsInfo selectByCondition(Map<String, Object> condition) {
		return deviceRoadMapper.selectByCondition(condition);
	}

}
