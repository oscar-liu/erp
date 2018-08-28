package com.whalegoods.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whalegoods.entity.Device;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.GoodsStorageIn;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.GoodsStorageInService;
import com.whalegoods.util.ReType;


/**
 * 后台进销存相关接口
 * @author henrysun
 * 2018年8月28日 下午2:09:59
 */
@Controller
@RequestMapping(value = "/storage")
public class GoodsStorageController  {
	
	 @Autowired
	 GoodsStorageInService goodsStorageInService; 
	 
	 @Autowired
	 DeviceService deviceService;
	 
	 @Autowired
	 GoodsSkuService goodsSkuService;

	  /**
	   * 跳转到仓库入库列表页面
	   * @author henrysun
	   * 2018年8月28日 下午3:00:08
	   */
	  @GetMapping(value = "showGoodsStorageIn")
	  @RequiresPermissions("storage:in:list")
	  public String showGoodsStorageIn(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/storage/in/storageInList";
	  }

	  /**
	   * 查询仓库入库列表接口
	   * @author henrysun
	   * 2018年8月28日 下午3:00:24
	   */
	  @GetMapping(value = "showGoodsStorageInList")
	  @ResponseBody
	  @RequiresPermissions("storage:in:list")
	  public ReType showGoodsStorageInList(Model model, GoodsStorageIn goodsStorageIn , String page, String limit) {
		  return goodsStorageInService.selectByPage(goodsStorageIn,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加货道信息页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddRoad")
	  public String showAddRoad(Model model) {
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/device/road/add-road";
	  }
	
}
