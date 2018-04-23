package com.whalegoods.mapper;


import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;

import java.util.List;
import java.util.Map;

public interface DeviceRoadMapper  {

    List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> condition);
    
    ResDeviceGoodsInfo selectByCondition(Map<String,Object> condition);
    
    int updateDeviceRoad(DeviceRoad model);
    
    int updateStockNoRepeat(DeviceRoad model);
}