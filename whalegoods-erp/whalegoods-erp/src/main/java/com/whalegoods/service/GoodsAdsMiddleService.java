package com.whalegoods.service;


import java.util.List;
import java.util.Map;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;
import com.whalegoods.exception.SystemException;

/**
 * 促销活动Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsMiddleService  extends BaseService<GoodsAdsMiddle,String>{
	
	List<GoodsAdsMiddle> selectAdsMiddleList(ReqGetAdsMiddleList objCdt) throws SystemException;
	
	List<GoodsAdsMiddle> selectTimeRangeByDeviceId(Map<String,Object> mapCdt);
	
	int insertBatch(List<GoodsAdsMiddle> list);
	
	List<String> selectNotUpDevice();
	
	List<Double> selectLastSalePrice(String goodsCode);
}
