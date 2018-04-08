package com.whalegoods.mapper;


import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResGoodsAdsTop;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GoodsAdsTopMapper extends BaseMapper<GoodsAdsTop,String> {

    @Override
    int deleteByPrimaryKey(String id);

    int insert(GoodsAdsTop record);

    @Override
    int insertSelective(GoodsAdsTop record);

    @Override
    GoodsAdsTop selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(GoodsAdsTop record);

    @Override
    int updateByPrimaryKey(GoodsAdsTop record);

    GoodsAdsTop login(@Param("username") String username);

    @Override
    List<GoodsAdsTop> selectListByPage(GoodsAdsTop GoodsAdsTop);

    int count();

    int add(GoodsAdsTop user);

    int delById(String id);

    int checkUser(String username);

    int rePass(GoodsAdsTop user);

    List<ResGoodsAdsTop> selectByIdOfJpAndSupp(Map<String,Object> condition);
}