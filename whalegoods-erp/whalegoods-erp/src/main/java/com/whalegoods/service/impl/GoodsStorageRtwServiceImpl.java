package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageRtw;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageRtwMapper;
import com.whalegoods.service.GoodsStorageRtwService;

@Service
public class GoodsStorageRtwServiceImpl extends BaseServiceImpl<GoodsStorageRtw,String> implements GoodsStorageRtwService {
	
	  @Autowired
	  GoodsStorageRtwMapper goodsStorageRtwMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorageRtw, String> getMapper() {
	    return goodsStorageRtwMapper;
	  }
	  
}
