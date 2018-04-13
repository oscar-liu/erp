package com.whalegoods.mapper;


import com.whalegoods.entity.OrderList;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrderListMapper extends BaseMapper<OrderList,String> {

    @Override
    int deleteByPrimaryKey(String id);

    @Override
    int insert(OrderList record);

    @Override
    int insertSelective(OrderList record);

    @Override
    OrderList selectByPrimaryKey(String id);

    @Override
    int updateByPrimaryKeySelective(OrderList record);

    @Override
    int updateByPrimaryKey(OrderList record);

    OrderList login(@Param("username") String username);

    @Override
    List<OrderList> selectListByPage(OrderList OrderPrepay);

    int count();

    int add(OrderList user);

    int delById(String id);

    int checkUser(String username);

    int rePass(OrderList user);
        
    /**
     * 根据订单号和订单状态查询预支付订单信息
     * @param order
     * @return
     */
    OrderList selectByOrderIdAndStatus(Map<String,Object> mapCdt);
    
    int updateOrderList(OrderList model);
    
    int insertOrderList(OrderList model);

}