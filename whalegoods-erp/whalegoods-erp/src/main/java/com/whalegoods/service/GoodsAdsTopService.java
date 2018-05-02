package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResGoodsAdsTop;


import java.util.List;
import java.util.Map;

/**
 * 顶部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsTopService extends BaseService<GoodsAdsTop,String> {
  
  List<ResGoodsAdsTop> selectAdsTopList(Map<String,Object> mapCdt);

}
