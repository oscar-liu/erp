package com.whalegoods.service;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.request.ReqGetAdsTopList;
import com.whalegoods.entity.response.ResGoodsAdsTop;

import java.util.List;

/**
 * 顶部广告Service接口层
 * @author henry-sun
 *
 */
public interface GoodsAdsTopService extends BaseService<GoodsAdsTop,String> {
  
	public List<ResGoodsAdsTop> selectAdsTopList(ReqGetAdsTopList objCdt);

}
