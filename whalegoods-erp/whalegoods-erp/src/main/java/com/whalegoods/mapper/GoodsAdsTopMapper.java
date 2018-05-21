package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.request.ReqGetAdsTopList;
import com.whalegoods.entity.response.ResGoodsAdsTop;

import java.util.List;


public interface GoodsAdsTopMapper extends BaseMapper<GoodsAdsTop,String> {
	
	/**
	 * 获取顶部广告列表
	 * @author henrysun
	 * 2018年5月2日 下午3:22:07
	 */
    List<ResGoodsAdsTop> selectAdsTopList(ReqGetAdsTopList objCdt);
}