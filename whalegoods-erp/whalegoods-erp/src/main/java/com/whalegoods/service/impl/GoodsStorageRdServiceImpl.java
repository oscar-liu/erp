package com.whalegoods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageRd;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageRdMapper;
import com.whalegoods.service.GoodsStorageRdService;

@Service
public class GoodsStorageRdServiceImpl extends BaseServiceImpl<GoodsStorageRd,String> implements GoodsStorageRdService {
	
	  @Autowired
	  GoodsStorageRdMapper goodsStorageRdMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorageRd, String> getMapper() {
	    return goodsStorageRdMapper;
	  }
	  
}
