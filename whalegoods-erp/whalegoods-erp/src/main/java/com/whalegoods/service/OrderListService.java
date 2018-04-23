package com.whalegoods.service;


import java.util.Map;


import com.whalegoods.entity.OrderList;


/**
 * 订单信息相关操作Service接口层
 * @author chencong
 *
 */
public interface OrderListService {
	
    /**
     * 根据条件查询订单信息
     * @param order
     * @return
     */
   public OrderList selectByCondition(Map<String,Object> mapCdt);
    
    /**
     * 根据条件更新订单信息
     * @author chencong
     * 2018年4月18日 下午4:36:11
     */
   public int updateOrderList(OrderList model);
    
    /**
     * 生成订单信息
     * @author chencong
     * 2018年4月18日 下午4:35:21
     */
   public int insertOrderList(OrderList model);
}
