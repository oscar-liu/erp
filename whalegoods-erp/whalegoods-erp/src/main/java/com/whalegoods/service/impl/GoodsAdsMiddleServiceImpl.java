package com.whalegoods.service.impl;

import java.util.List;
import java.util.Map;

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

	@Override
	public List<GoodsAdsMiddle> selectTimeRangeByDeviceId(Map<String, Object> mapCdt) {
		return GoodsAdsMiddleMapper.selectTimeRangeByDeviceId(mapCdt);
	}

	@Override
	public int insertBatch(List<GoodsAdsMiddle> list) {
		return GoodsAdsMiddleMapper.insertBatch(list);
	}

	@Override
	public List<String> selectNotUpDevice() {
		return GoodsAdsMiddleMapper.selectNotUpDevice();
	}

	@Override
	public List<Double> selectLastSalePrice(String goodsCode) {
		return GoodsAdsMiddleMapper.selectLastSalePrice(goodsCode);
	}

	@Override
	public int updateKeepSamePrice(GoodsAdsMiddle objCdt) {
		return GoodsAdsMiddleMapper.updateKeepSamePrice(objCdt);
	}

}
