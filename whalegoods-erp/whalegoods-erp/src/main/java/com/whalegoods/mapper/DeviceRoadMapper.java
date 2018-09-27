package com.whalegoods.mapper;


import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;

import java.util.List;
import java.util.Map;

public interface DeviceRoadMapper extends BaseMapper<DeviceRoad,String>  {

    List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String,Object> mapCdt);
    
    List<ResDeviceGoodsInfo> selectByGoodsOrPathCode(Map<String,Object> mapCdt);

    int updateByObjCdtForErp(DeviceRoad objCdt);
    
    int deleteByDeviceId(String deviceId);
    
    int insertBatch(List<DeviceRoad> list);
    
    List<DeviceRoad> selectListByDeviceIdForExcel(String deviceId);
    
    int updateSalePrice(Map<String,Object> mapCdt);
    
    double selectMaxPriceByGoodsCode(Map<String,Object> mapCdt);
    
    /**
     * 根据商品编号找到最新的从高到低的三个价格
     * @author henrysun
     * 2018年9月3日 上午4:44:34
     */
    List<Double> selectLastSalePrice(String goodsCode);
    
    /**
     * 将设备货道绑定出库批次
     * @author henrysun
     * 2018年9月27日 下午6:32:48
     */
    int updateBindingStorageOut(DeviceRoad objCdt);
    
}