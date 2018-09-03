package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorage;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageMapper;
import com.whalegoods.service.GoodsStorageService;

@Service
public class GoodsStorageServiceImpl extends BaseServiceImpl<GoodsStorage,String> implements GoodsStorageService {
	
	  @Autowired
	  GoodsStorageMapper goodsStorageMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorage, String> getMapper() {
	    return goodsStorageMapper;
	  }
	  
}
