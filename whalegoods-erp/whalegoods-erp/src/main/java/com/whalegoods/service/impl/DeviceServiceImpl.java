package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceMapper;
import com.whalegoods.service.DeviceService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;


@Service
public class DeviceServiceImpl  implements DeviceService {
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	DeviceMapper deviceMapper;


	@Override
	public void updateDevice(Device model) throws SystemException {
		try {
			deviceMapper.updateByCondition(model);
		} catch (Exception e) {
			logger.error("更新设备信息失败："+model.toString()+" 原因："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
	}

	@Override
	public int getOperateStatus(Map<String, Object> condition) {
		return deviceMapper.getOperateStatus(condition);
	}

	@Override
	public ResBody getApk(Map<String, Object> condition) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		resBody.setData(deviceMapper.getApk(condition));
		return resBody;
	}

	@Override
	public Device selectById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Device> selectListByCondition(Device cdt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectByPage(Device t, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByCondition(Device cdt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Device record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteById(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysUser login(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Device> selectListByPage(Device sysUser) {
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
	public int updateByPrimaryKey(Device sysUser) {
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
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
