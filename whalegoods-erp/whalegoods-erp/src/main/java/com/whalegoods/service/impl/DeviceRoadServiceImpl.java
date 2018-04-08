package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;


@Service
public class DeviceRoadServiceImpl extends BaseServiceImpl<DeviceRoad,String> implements DeviceRoadService {
	
	@Autowired
	DeviceRoadMapper deviceRoadMapper;

	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysUser login(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceRoad selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(DeviceRoad user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JsonUtil delById(String id, boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int checkUser(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Checkbox> getUserRoleByJson(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int rePass(DeviceRoad user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseMapper<DeviceRoad,String> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String, Object> condition) {
		return deviceRoadMapper.selectByIdOfJpAndSupp(condition);
	}

	@Override
	public ResDeviceGoodsInfo selectByPathOrGoodsCode(Map<String, Object> condition) {
		ResDeviceGoodsInfo model=deviceRoadMapper.selectByPathOrGoodsCode(condition); 
		return model;
	}

}
