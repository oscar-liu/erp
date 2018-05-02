package com.whalegoods.service;


import com.whalegoods.exception.SystemException;

import java.util.Map;

/**
 * 中部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsMiddleService  {
  
  Map<String, Object> selectAdsMiddleList(Map<String,Object> condition) throws SystemException;


}
