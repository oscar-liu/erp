package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageIr;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageIrMapper;
import com.whalegoods.service.GoodsStorageIrService;

@Service
public class GoodsStorageIrServiceImpl extends BaseServiceImpl<GoodsStorageIr,String> implements GoodsStorageIrService {
	
	  @Autowired
	  GoodsStorageIrMapper goodsStorageIrMapper;
	  
	  @Override
	  public BaseMapper<GoodsStorageIr, String> getMapper() {
	    return goodsStorageIrMapper;
	  }
	  
}
