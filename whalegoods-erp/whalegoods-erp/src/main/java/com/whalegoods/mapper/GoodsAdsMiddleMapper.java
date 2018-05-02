package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;

import java.util.List;
import java.util.Map;

public interface GoodsAdsMiddleMapper extends BaseMapper<GoodsAdsMiddle,String> {

	/**
	 * 获取柜机展示的中部促销商品列表
	 * @author henrysun
	 * 2018年5月2日 下午3:20:16
	 */
    List<ResGoodsAdsMiddle> selectAdsMiddleList(Map<String,Object> condition);
}