package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsAdsTopMapper;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.JsonUtil;


@Service
public class GoodsAdsTopServiceImpl extends BaseServiceImpl<GoodsAdsTop,String> implements GoodsAdsTopService {
	
	@Autowired
	GoodsAdsTopMapper GoodsAdsTopMapper;

	@Override
	public List<ResGoodsAdsTop> selectByIdOfJpAndSupp(Map<String, Object> condition) {
		return GoodsAdsTopMapper.selectByIdOfJpAndSupp(condition);
	}

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
	public GoodsAdsTop selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(GoodsAdsTop user) {
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
	public int rePass(GoodsAdsTop user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseMapper<GoodsAdsTop,String> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
