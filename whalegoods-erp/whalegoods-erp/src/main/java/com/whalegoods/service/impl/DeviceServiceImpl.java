package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.Device;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceMapper;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;


@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device,String> implements DeviceService {
	
	@Autowired
	DeviceMapper deviceMapper;

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
	public Device selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(Device user) {
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
	public int rePass(Device user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseMapper<Device,String> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateDeviceStatus(Map<String, Object> condition) {
		return deviceMapper.updateDeviceStatus(condition);
	}

}
