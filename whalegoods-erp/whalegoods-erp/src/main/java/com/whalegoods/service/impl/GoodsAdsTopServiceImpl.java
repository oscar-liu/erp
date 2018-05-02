package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsAdsTopMapper;
import com.whalegoods.service.GoodsAdsTopService;

@Service
public class GoodsAdsTopServiceImpl extends BaseServiceImpl<GoodsAdsTop,String> implements GoodsAdsTopService {
	
	@Autowired
	GoodsAdsTopMapper GoodsAdsTopMapper;
	
	@Override
	public BaseMapper<GoodsAdsTop,String> getMapper() {
		return GoodsAdsTopMapper;
	}

	@Override
	public List<ResGoodsAdsTop> selectAdsTopList(Map<String, Object> condition) {
		return GoodsAdsTopMapper.selectAdsTopList(condition);
	}

}
