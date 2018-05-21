package com.whalegoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;
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
	public List<GoodsAdsMiddle> selectAdsMiddleList(ReqGetAdsMiddleList objCdt) throws SystemException {
		 return GoodsAdsMiddleMapper.selectAdsMiddleList(objCdt);
	}

}
