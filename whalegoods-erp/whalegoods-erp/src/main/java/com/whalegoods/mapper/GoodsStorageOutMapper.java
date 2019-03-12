package com.whalegoods.mapper;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsStorageOut;


public interface GoodsStorageOutMapper extends BaseMapper<GoodsStorageOut, String>{
	
	List<GoodsStorageOut> selectListByObjCdtForSetDeviceRoad(GoodsStorageOut objCdt);
	
	String selectTopOneOutIdByMapCdt(Map<String,Object> mapCdt);
	
	String selectTopOneOutIdByMapCdtSpecial(DeviceRoad mapCdt);	
}