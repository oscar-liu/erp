package com.whalegoods.service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;

import java.util.List;
import java.util.Map;

/**
 * 货道信息Service接口层
 * @author henry-sun
 *
 */
public interface DeviceRoadService extends BaseService<DeviceRoad,String> {
  
  List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> condition);
  
  ResDeviceGoodsInfo selectByCondition(Map<String,Object> condition);

}
