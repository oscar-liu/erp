package com.whalegoods.controller;

import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.service.OrderListService;
import com.whalegoods.util.ReType;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台系统订单相关API接口
 * @author henrysun
 * 2018年4月25日 下午7:59:49
 */
@Controller
@RequestMapping(value = "/order")
public class OrderListController {
	
	  @Autowired
	  OrderListService orderListService;

	  /**
	   * 跳转到订单列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showOrder")
	  @RequiresPermissions("order:show")
	  public String showUser(Model model) {
	    return "/order/ ";
	  }

	  /**
	   * 查询订单列表列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	   */
	  @GetMapping(value = "showOrderList")
	  @ResponseBody
	  @RequiresPermissions("order:show") 
	  public ReType showUser(Model model, ErpOrderList orderList, String page, String limit) {
		orderList.setPrefix(orderList.getStartOrderTime().substring(0,7));
	    return orderListService.selectByPage(orderList,Integer.valueOf(page),Integer.valueOf(limit));
	  }


}
