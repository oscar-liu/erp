package com.whalegoods.mapper;


import java.util.List;

import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;

public interface GoodsAdsMiddleMapper extends BaseMapper<GoodsAdsMiddle,String> {
	
	/**
	 * 获取柜机展示的中部促销商品列表
	 * @author henrysun
	 * 2018年5月2日 下午3:20:16
	 */
    List<GoodsAdsMiddle> selectAdsMiddleList(ReqGetAdsMiddleList objCdt);
}