package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsTop;

import java.util.List;
import java.util.Map;

/**
 * 顶部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsTopService extends BaseService<GoodsAdsTop,String> {
  
  List<Map<String, Object>> selectAdsTopList(Map<String,Object> mapCdt);

}
