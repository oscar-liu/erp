package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.exception.SystemException;

import java.util.List;
import java.util.Map;

/**
 * 促销活动Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsMiddleService  extends BaseService<GoodsAdsMiddle,String>{
  
	List<ResGoodsAdsMiddle> selectAdsMiddleList(Map<String,Object> mapCdt,Map<String, Object> mapData) throws SystemException;
	
}
