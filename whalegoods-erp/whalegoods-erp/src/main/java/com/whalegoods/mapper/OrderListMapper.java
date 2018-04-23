package com.whalegoods.mapper;


import com.whalegoods.entity.OrderList;

import java.util.Map;


public interface OrderListMapper {

    /**
     * 根据条件查询订单信息
     * @param order
     * @return
     */
    OrderList selectByCondition(Map<String,Object> mapCdt);
    
    /**
     * 根据条件更新订单信息
     * @author chencong
     * 2018年4月18日 下午4:36:11
     */
    int updateOrderList(OrderList model);
    
    /**
     * 生成预支付订单
     * @author chencong
     * 2018年4月18日 下午4:35:21
     */
    int insertOrderList(OrderList model);

}