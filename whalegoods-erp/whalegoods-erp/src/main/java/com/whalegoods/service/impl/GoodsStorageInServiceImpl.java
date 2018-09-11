package com.whalegoods.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageIn;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageInMapper;
import com.whalegoods.service.GoodsStorageInService;

@Service
public class GoodsStorageInServiceImpl extends BaseServiceImpl<GoodsStorageIn,String> implements GoodsStorageInService {
	
	  @Autowired
	  GoodsStorageInMapper goodsStorageInMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorageIn, String> getMapper() {
	    return goodsStorageInMapper;
	  }

	@Override
	public List<GoodsStorageIn> getStorageInListByGoodsSkuId(String goodsSkuId) {
		 return goodsStorageInMapper.getStorageInListByGoodsSkuId(goodsSkuId);
	}

	@Override
	public List<Double> selectLastMarketPrice(String goodsCode) {
		 return goodsStorageInMapper.selectLastMarketPrice(goodsCode);
	}
	  
}