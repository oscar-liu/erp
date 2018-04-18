package com.whalegoods.mapper;


import com.whalegoods.entity.OrderList;

import java.util.Map;


public interface OrderListMapper extends BaseMapper<OrderList,String> {

    /**
     * 根据订单号和订单状态查询预支付订单信息
     * @param order
     * @return
     */
    OrderList selectByCondition(Map<String,Object> mapCdt);
    
    /**
     * 更新数据
     * @author chencong
     * 2018年4月18日 下午4:36:11
     */
    int updateOrderList(OrderList model);
    
    /**
     * 添加数据
     * @author chencong
     * 2018年4月18日 下午4:35:21
     */
    int insertOrderList(OrderList model);

}