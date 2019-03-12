package com.whalegoods.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
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

	@Override
	public List<GoodsStorageOut> selectListByObjCdtForSetDeviceRoad(GoodsStorageOut objCdt) {
		return goodsStorageOutMapper.selectListByObjCdtForSetDeviceRoad(objCdt);
	}

	@Override
	public String selectTopOneOutIdByMapCdt(Map<String, Object> mapCdt) {
		return goodsStorageOutMapper.selectTopOneOutIdByMapCdt(mapCdt);
	}

	@Override
	public String selectTopOneOutIdByMapCdtSpecial(DeviceRoad objCdt) {
		return goodsStorageOutMapper.selectTopOneOutIdByMapCdtSpecial(objCdt);
	}
	  
}
