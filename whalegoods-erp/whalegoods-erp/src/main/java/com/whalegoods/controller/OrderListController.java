package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	  
	  @Autowired
	  DeviceService deviceService;
	  
	  @Autowired
	  public PayService payService;

	  /**
	   * 跳转到订单列表页面
	   * @author henrysun
	   * 2018年4月26日 下午3:29:12
	   */
	  @GetMapping(value = "showOrder")
	  @RequiresPermissions("order:show")
	  public String showUser(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/order/orderList";
	  }

	  /**
	   * 查询订单列表列表接口
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	 * @throws SystemException 
	   */
	  @GetMapping(value = "showOrderList")
	  @ResponseBody
	  @RequiresPermissions("order:show") 
	  public ReType showOrderList(Model model, ErpOrderList orderList, String page, String limit) throws SystemException {
		if(orderList.getOrderType()!=null){
			if(orderList.getOrderType()==2)
			{
				orderList.setCreateBy(ShiroUtil.getCurrentUserId());		
			}
		}
		return orderListService.selectByPage(orderList,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到销售统计>按设备页面
	   * @author henrysun
	   * 2018年6月21日 下午6:11:29
	   */
	  @GetMapping(value = "showReportByDevice")
	  @RequiresPermissions("order:report:byDevice")
	  public String showReportByDevice(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/order/report/reportByDeviceList";
	  }
	  
	  /**
	   * 刷单确认
	   * @author henrysun
	   * 2018年5月17日 上午10:54:08
	   */
	  @GetMapping(value="/sdConfirm")
	  @ResponseBody
	  ResBody sdConfirm(@RequestParam String orderId) throws SystemException  {
		  return payService.getOrderStatus(orderId);
		}
	  
	  /**
	   * 导出刷单记录
	   * @author henrysun
	   * 2018年5月17日 上午10:54:08
	   */
	  @GetMapping(value="/sdExcel")
	  void sdExcel(ErpOrderList orderList,HttpServletResponse response) throws SystemException  {
		  orderList.setCreateBy(ShiroUtil.getCurrentUserId());
		  orderList.setOrderType((byte) 2);
		  List<ErpOrderList> resultList=orderListService.getListByObjCdt(orderList);
		  if(resultList.size()==0){
			  throw new BizApiException(ConstApiResCode.SDLIST_EMPTY);
		  }
		  FileUtil.exportExcel(resultList,"刷单记录","刷单记录",ErpOrderList.class,"刷单记录.xls",response);
		}

}
