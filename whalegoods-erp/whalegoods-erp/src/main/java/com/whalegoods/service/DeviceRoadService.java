package com.whalegoods.service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.SystemException;

import java.util.List;
import java.util.Map;

/**
 * 货道信息Service接口层
 * @author henry-sun
 *
 */
public interface DeviceRoadService extends BaseService<DeviceRoad,String> {
  
  List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> mapCdt) throws SystemException;
  
  List<ResDeviceGoodsInfo> selectByGoodsOrPathCode(Map<String,Object> mapCdt);

  int updateByObjCdtForErp(DeviceRoad objCdt);
  
  int deleteByDeviceId(String deviceId);
  
  int insertBatch(List<DeviceRoad> list);
  
  List<DeviceRoad> selectListByDeviceIdForExcel(String deviceId);
  
	int updateSalePrice(Map<String,Object> mapCdt);
	
  double selectMaxPriceByGoodsCode(Map<String,Object> mapCdt);
  
  List<Double> selectLastSalePrice(String goodsCode);
  
  int updateBindingStorageOut(DeviceRoad objCdt);
 
}
