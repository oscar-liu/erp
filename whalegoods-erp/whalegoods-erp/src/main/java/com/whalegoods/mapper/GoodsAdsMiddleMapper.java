package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;

import java.util.List;
import java.util.Map;

public interface GoodsAdsMiddleMapper extends BaseMapper<GoodsAdsMiddle,String> {

    List<ResGoodsAdsMiddle> selectByDeviceId(Map<String,Object> condition);
}