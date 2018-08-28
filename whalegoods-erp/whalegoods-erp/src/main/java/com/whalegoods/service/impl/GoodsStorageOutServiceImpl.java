package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageOut;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageOutMapper;
import com.whalegoods.service.GoodsStorageOutService;

@Service
public class GoodsStorageOutServiceImpl extends BaseServiceImpl<GoodsStorageOut,String> implements GoodsStorageOutService {
	
	  @Autowired
	  GoodsStorageOutMapper goodsStorageOutMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorageOut, String> getMapper() {
	    return goodsStorageOutMapper;
	  }
	  
}
