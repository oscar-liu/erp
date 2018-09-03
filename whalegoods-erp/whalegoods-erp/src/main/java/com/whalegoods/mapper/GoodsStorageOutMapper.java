package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.GoodsStorageOut;


public interface GoodsStorageOutMapper extends BaseMapper<GoodsStorageOut, String>{
	
	List<GoodsStorageOut> selectListByObjCdtForSetDeviceRoad(GoodsStorageOut objCdt);
}