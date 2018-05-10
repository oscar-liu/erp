package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsSku;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsSkuMapper;
import com.whalegoods.service.GoodsSkuService;

@Service
public class GoodsSkuServiceImpl extends BaseServiceImpl<GoodsSku,String> implements GoodsSkuService {
	
	  @Autowired
	  GoodsSkuMapper goodsSkuMapper;
	  
	  @Override
	  public BaseMapper<GoodsSku, String> getMapper() {
	    return goodsSkuMapper;
	  }

	@Override
	public Boolean checkGoodsCode(String goodsCode) {
		if(goodsSkuMapper.checkGoodsCode(goodsCode)>0){
			return true;
		}
		return false;
	}
	  
}
