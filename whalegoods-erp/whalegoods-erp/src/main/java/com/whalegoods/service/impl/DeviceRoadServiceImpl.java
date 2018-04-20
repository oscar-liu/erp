package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;


@Service
public class DeviceRoadServiceImpl extends BaseServiceImpl<DeviceRoad,String> implements DeviceRoadService {

	private static Logger logger = LoggerFactory.getLogger(DeviceRoadServiceImpl.class);
	
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
		List<ResDeviceGoodsInfo> list=deviceRoadMapper.selectByIdOfJpAndSupp(condition);
		for (ResDeviceGoodsInfo resDeviceGoodsInfo : list) {
			//右连接查询，如果为空，需要做此转换
			resDeviceGoodsInfo.setType((byte) (resDeviceGoodsInfo.getType()!=null?1:2));
		}
		return list;
	}

	@Override
	public ResDeviceGoodsInfo selectByCondition(Map<String, Object> condition) {
		ResDeviceGoodsInfo model=deviceRoadMapper.selectByCondition(condition); 
		return model;
	}

	@Transactional
	@Override
	public void updateDeviceRoad(DeviceRoad model) throws SystemException {
		try {
			deviceRoadMapper.updateDeviceRoad(model);
		} catch (Exception e) {
			logger.error("更新货道信息失败："+model.toString()+" 原因："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	}

}
