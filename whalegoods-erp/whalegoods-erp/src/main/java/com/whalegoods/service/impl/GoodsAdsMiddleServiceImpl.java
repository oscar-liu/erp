package com.whalegoods.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.SysRoleUser;
import com.whalegoods.entity.SysUser;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddleJson;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsAdsMiddleMapper;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.util.Checkbox;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.JsonUtil;


@Service
public class GoodsAdsMiddleServiceImpl extends BaseServiceImpl<GoodsAdsMiddle,String> implements GoodsAdsMiddleService {
	
	@Autowired
	GoodsAdsMiddleMapper GoodsAdsMiddleMapper;
	
	@Override
	public Map<String,Object> selectByDeviceRoadId(Map<String, Object> condition) throws IllegalAccessException, InvocationTargetException {
		List<ResGoodsAdsMiddle> list=GoodsAdsMiddleMapper.selectByDeviceRoadId(condition);
		List<ResGoodsAdsMiddleJson> list1=new ArrayList<>();
		List<ResGoodsAdsMiddleJson> list2=new ArrayList<>();
		Map<String,Object> map=new HashMap<>();
		for (ResGoodsAdsMiddle item : list) {
			ResGoodsAdsMiddleJson object=new ResGoodsAdsMiddleJson();
			BeanUtils.copyProperties(object,item);
			if(item.getType()==1)
			{
				//计算剩余时间，到秒
				int restTime=DateUtil.secsSub(item.getStartTime(), item.getEndTime());
				map.put("rest_time", restTime);
				list1.add(object);
				map.put("now", list1);
			}
			if(item.getType()==2)
			{
				list2.add(object);
				map.put("next", list2);
			}
		}
		return map;
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
	public GoodsAdsMiddle selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(GoodsAdsMiddle user) {
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
	public int rePass(GoodsAdsMiddle user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BaseMapper<GoodsAdsMiddle,String> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
