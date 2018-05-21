package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.request.ReqGetAdsTopList;
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
	public List<ResGoodsAdsTop> selectAdsTopList(ReqGetAdsTopList objCdt) {
		return GoodsAdsTopMapper.selectAdsTopList(objCdt);
	}

}
