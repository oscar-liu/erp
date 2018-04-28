package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResGoodsAdsTop;

import java.util.List;
import java.util.Map;


public interface GoodsAdsTopMapper extends BaseMapper<GoodsAdsTop,String> {
	
    List<ResGoodsAdsTop> selectByIdOfJpAndSupp(Map<String,Object> condition);
}