package com.whalegoods.mapper;


import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;

import java.util.List;
import java.util.Map;

public interface DeviceRoadMapper extends BaseMapper<DeviceRoad,String>  {

    List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> condition);
    
    ResDeviceGoodsInfo selectByCondition(Map<String,Object> condition);
    
}