package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsAdsMiddleMapper;
import com.whalegoods.service.GoodsAdsMiddleService;


@Service
public class GoodsAdsMiddleServiceImpl extends BaseServiceImpl<GoodsAdsMiddle,String> implements GoodsAdsMiddleService {
	
	@Autowired
	GoodsAdsMiddleMapper GoodsAdsMiddleMapper;
	
	@Override
	public BaseMapper<GoodsAdsMiddle,String> getMapper() {
		return GoodsAdsMiddleMapper;
	}
	
	@Override
	public List<ResGoodsAdsMiddle> selectAdsMiddleList(Map<String, Object> mapCdt,Map<String, Object> mapData) throws SystemException {
		 return GoodsAdsMiddleMapper.selectAdsMiddleList(mapCdt);
	}

}
