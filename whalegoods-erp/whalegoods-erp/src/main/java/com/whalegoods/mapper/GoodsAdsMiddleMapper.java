package com.whalegoods.mapper;


import java.util.List;
import java.util.Map;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;

public interface GoodsAdsMiddleMapper extends BaseMapper<GoodsAdsMiddle,String> {
	
	/**
	 * 获取柜机展示的中部促销商品列表
	 * @author henrysun
	 * 2018年5月2日 下午3:20:16
	 */
    List<GoodsAdsMiddle> selectAdsMiddleList(ReqGetAdsMiddleList objCdt);
    
    /**
     * 查询当前设备所有的促销时间段
     * @author henrysun
     * 2018年6月12日 下午10:06:42
     */
    List<GoodsAdsMiddle> selectTimeRangeByDeviceId(Map<String,Object> mapCdt);
    
    int insertBatch(List<GoodsAdsMiddle> list);
    
    /**
     * 查询未上架商品的设备
     * @return
     */
    List<String> selectNotUpDevice();
    
    /**
     * 根据商品编号找到最新的三个促销价格
     * @return
     */
    List<Double> selectLastSalePrice(String goodsCode);
    
    /**
     * 保持相同设备相同的促销商品价格一致
     * @author henrysun
     * 2018年8月16日 下午6:41:28
     */
    int updateKeepSamePrice(GoodsAdsMiddle objCdt);
}