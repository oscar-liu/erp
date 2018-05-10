package com.whalegoods.mapper;


import java.util.List;

import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;


public interface OrderListMapper extends BaseMapper<OrderList,String> {

    List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt);
}