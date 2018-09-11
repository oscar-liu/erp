package com.whalegoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsStorageLocation;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsStorageLocationMapper;
import com.whalegoods.service.GoodsStorageLocationService;


@Service
public class GoodsStorageLocationImpl extends BaseServiceImpl<GoodsStorageLocation,String>  implements GoodsStorageLocationService {
	
	@Autowired
	GoodsStorageLocationMapper goodsStorageLocationMapper;
	
	@Override
	public BaseMapper<GoodsStorageLocation, String> getMapper() {
		return goodsStorageLocationMapper;
	}

}
