package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GoodsAdsMiddleMapper extends BaseMapper<GoodsAdsMiddle,String> {

    @Override
    int deleteByPrimaryKey(String id);

    int insert(GoodsAdsMiddle record);

    @Override
    int insertSelective(GoodsAdsMiddle record);

    @Override
    GoodsAdsMiddle selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(GoodsAdsMiddle record);

    @Override
    int updateByPrimaryKey(GoodsAdsMiddle record);

    GoodsAdsMiddle login(@Param("username") String username);

    @Override
    List<GoodsAdsMiddle> selectListByPage(GoodsAdsMiddle GoodsAdsMiddle);

    int count();

    int add(GoodsAdsMiddle user);

    int delById(String id);

    int checkUser(String username);

    int rePass(GoodsAdsMiddle user);

    List<ResGoodsAdsMiddle> selectByDeviceId(Map<String,Object> condition);
}