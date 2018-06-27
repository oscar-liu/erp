package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.ReportBase;
import com.whalegoods.entity.ReportByDevice;
import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.entity.ReportTotalCountAndAmount;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.PayService;
import com.whalegoods.service.ReportBaseService;
import com.whalegoods.service.ReportByDeviceService;
import com.whalegoods.service.ReportByGoodsService;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

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
	  private OrderListService orderListService;
	  
	  @Autowired
	  private DeviceService deviceService;
	  
	  @Autowired
	  private GoodsSkuService goodsSkuService;
	  
	  @Autowired
	  private PayService payService;
	  
	  @Autowired
	  private ReportByDeviceService reportByDeviceService;
	  
	  @Autowired
	  private ReportByGoodsService reportByGoodsService;
	  
	  @Autowired
	  private ReportBaseService reportBaseService;

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
			if(orderList.getOrderType()==2){
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
	  @RequiresPermissions("order:report:byDevice:show")
	  public String showReportByDevice(Model model) {
		ReportTotalCountAndAmount total=reportBaseService.selectTotalSalesCountAndAmount(new ReportBase());
		List<Device> listDevice=deviceService.selectListByObjCdt(new Device());
		model.addAttribute("deviceList",listDevice);
		model.addAttribute("total",total);
	    return "/order/report/reportByDeviceList";
	  }
	  
	  /**
	   * 查询销售统计>按设备列表
	   * @author henrysun
	   * 2018年4月26日 下午3:29:23
	 * @throws SystemException 
	   */
	  @GetMapping(value = "showReportByDeviceList")
	  @ResponseBody
	  @RequiresPermissions("order:report:byDevice:show") 
	  public ReType showReportByDeviceList(Model model, ReportByDevice reportByDevice, String page, String limit) throws SystemException {
		if(!StringUtil.isEmpty(reportByDevice.getDayRange())){
				reportByDevice.setStartOrderDay(reportByDevice.getDayRange().split(ConstSysParamName.KGANG)[0]);
				reportByDevice.setEndOrderDay(reportByDevice.getDayRange().split(ConstSysParamName.KGANG)[1]);
		}
		return reportByDeviceService.selectByPage(reportByDevice,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到销售统计>明细 列表
	   * @author henrysun
	   * 2018年6月25日 下午12:29:11
	   */
	  @GetMapping(value = "showReportBaseDetail")
	  @RequiresPermissions("order:reportDetail:show")
	  public String showReportBaseDetail(Model model) {
		ReportTotalCountAndAmount total=reportBaseService.selectTotalSalesCountAndAmount(new ReportBase());
		List<Device> listDevice=deviceService.selectListByObjCdt(new Device());
		List<GoodsSku> listGoods=goodsSkuService.selectListByObjCdt(new GoodsSku());
		model.addAttribute("deviceList",listDevice);
		model.addAttribute("goodsList",listGoods);
		model.addAttribute("total",total);
	    return "/order/report/reportBaseDetailList";
	  }
	  
	  /**
	   * 查询销售统计>明细 列表
	   * @author henrysun
	   * 2018年6月25日 下午12:29:27
	   */
	  @GetMapping(value = "showReportBaseDetailList")
	  @ResponseBody
	  @RequiresPermissions("order:reportDetail:show") 
	  public ReType showReportBaseDetailList(Model model, ReportBase reportBase, String page, String limit) throws SystemException {
		if(!StringUtil.isEmpty(reportBase.getDayRange())){
			reportBase.setStartOrderDay(reportBase.getDayRange().split(ConstSysParamName.KGANG)[0]);
			reportBase.setEndOrderDay(reportBase.getDayRange().split(ConstSysParamName.KGANG)[1]);
		}
		return reportBaseService.selectByPage(reportBase,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 获取总销量和总销售额
	   * @author henrysun
	   * 2018年6月27日 下午2:35:56
	   */
	  @GetMapping(value = "getTotalCountAndAmount")
	  @ResponseBody
	  public ReportTotalCountAndAmount getTotalCountAndAmount(ReportBase reportBase) {
		 if(!StringUtil.isEmpty(reportBase.getDayRange())){
				reportBase.setStartOrderDay(reportBase.getDayRange().split(ConstSysParamName.KGANG)[0]);
				reportBase.setEndOrderDay(reportBase.getDayRange().split(ConstSysParamName.KGANG)[1]);
		  }
		  return reportBaseService.selectTotalSalesCountAndAmount(reportBase);
	  }
	  
	  /**
	   * 跳转到销售统计>按商品 页面
	   * @author henrysun
	   * 2018年6月25日 下午2:22:18
	   */
	  @GetMapping(value = "showReportByGoods")
	  @RequiresPermissions("order:report:byGoods:show")
	  public String showReportByGoods(Model model) {
		ReportTotalCountAndAmount total=reportBaseService.selectTotalSalesCountAndAmount(new ReportBase());
		List<GoodsSku> listGoods=goodsSkuService.selectListByObjCdt(new GoodsSku());
		model.addAttribute("goodsList",listGoods);
		model.addAttribute("total",total);
	    return "/order/report/reportByGoodsList";
	  }
	  
	  /**
	   * 查询销售统计>按商品 列表
	   * @author henrysun
	   * 2018年6月25日 下午2:22:53
	   */
	  @GetMapping(value = "showReportByGoodsList")
	  @ResponseBody
	  @RequiresPermissions("order:report:byGoods:show") 
	  public ReType showReportByGoodsList(Model model, ReportByGoods reportByGoods, String page, String limit) throws SystemException {
		if(!StringUtil.isEmpty(reportByGoods.getDayRange())){
			reportByGoods.setStartOrderDay(reportByGoods.getDayRange().split(ConstSysParamName.KGANG)[0]);
			reportByGoods.setEndOrderDay(reportByGoods.getDayRange().split(ConstSysParamName.KGANG)[1]);
		}
		return reportByGoodsService.selectByPage(reportByGoods,Integer.valueOf(page),Integer.valueOf(limit));
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
