package com.whalegoods.mapper;


import com.whalegoods.entity.OrderPrepay;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderPrepayMapper extends BaseMapper<OrderPrepay,String> {

    @Override
    int deleteByPrimaryKey(String id);

    int insert(OrderPrepay record);

    @Override
    int insertSelective(OrderPrepay record);

    @Override
    OrderPrepay selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(OrderPrepay record);

    @Override
    int updateByPrimaryKey(OrderPrepay record);

    OrderPrepay login(@Param("username") String username);

    @Override
    List<OrderPrepay> selectListByPage(OrderPrepay OrderPrepay);

    int count();

    int add(OrderPrepay user);

    int delById(String id);

    int checkUser(String username);

    int rePass(OrderPrepay user);

}