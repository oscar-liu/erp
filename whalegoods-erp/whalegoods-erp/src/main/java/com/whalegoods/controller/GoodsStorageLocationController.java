package com.whalegoods.controller;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.GoodsStorageLocation;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsStorageLocationService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品仓库库位管理接口
 * @author henrysun
 * 2018年9月4日 下午4:20:41
 */
@Controller
@RequestMapping(value = "/storage")
public class GoodsStorageLocationController {
	
	@Autowired
	private GoodsStorageLocationService goodsStorageLocationService;

	  /**
	   * 跳转到库位列表页面
	   * @author henrysun
	   * 2018年9月4日 下午4:25:45
	   */
	  @GetMapping(value = "showGoodsStorageLocation")
	  @RequiresPermissions("goods:storage:location:show")
	  public String showDevice(Model model) {
	    return "/storage/location/storageLocationList";
	  }

	  /**
	   * 查询库位列表接口
	   * @author henrysun
	   * 2018年9月4日 下午4:26:36
	   */
	  @GetMapping(value = "showGoodsStorageLocationList")
	  @ResponseBody
	  @RequiresPermissions("goods:storage:location:show")
	  public ReType showGoodsStorageLocationList(Model model, GoodsStorageLocation goodsStorageLocation, String page, String limit) {
	    return goodsStorageLocationService.selectByPage(goodsStorageLocation,Integer.valueOf(page),Integer.valueOf(limit));
	  }

	  /**
	   * 跳转到添加库位页面
	   * @author henrysun
	   * 2018年9月4日 下午4:45:07
	   */
	  @GetMapping(value = "showAddStorageLocation")
	  public String showAddStorageLocation(Model model) {
		  return "/storage/location/add-storageLocation";
	  }

	  /**
	   * 添加库位接口
	   * @author henrysun
	   * 2018年9月4日 下午5:20:19
	   */
	  @PostMapping(value = "addStorageLocation")
	  @ResponseBody
	  public ResBody addDevice(GoodsStorageLocation goodsStorageLocation) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		goodsStorageLocation.setId(StringUtil.getUUID());
    	goodsStorageLocation.setCreateBy(ShiroUtil.getCurrentUserId());
    	goodsStorageLocation.setUpdateBy(ShiroUtil.getCurrentUserId());
    	goodsStorageLocationService.insert(goodsStorageLocation);
	    return resBody;
	  }

	  /**
	   * 跳转到更新库位页面
	   * @author henrysun
	   * 2018年4月26日 下午3:30:35
	   */
	  @GetMapping(value = "showUpdateStorageLocation")
	  public String showUpdateStorageLocation(String id, Model model){
		GoodsStorageLocation goodsStorageLocation=goodsStorageLocationService.selectById(id);
		model.addAttribute("goodsStorageLocation",goodsStorageLocation);
		return "/storage/location/update-storageLocation";
	  }
	  
	
	  /**
	   * 更新库位接口
	   * @author henrysun
	   * 2018年9月4日 下午5:43:09
	   */
	  @PostMapping(value = "updateStorageLocation")
	  @ResponseBody
	  public ResBody updateStorageLocation(@RequestBody GoodsStorageLocation goodsStorageLocation) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  goodsStorageLocationService.updateByObjCdt(goodsStorageLocation);
		  return resBody;
	  }
	  
	  /**
	   * 删除库位接口
	   * @author henrysun
	   * 2018年9月4日 下午5:43:37
	   */
	  @PostMapping(value = "/delStorageLocation")
	  @ResponseBody
	  @RequiresPermissions("goods:storage:location:del")
	  public ResBody delStorageLocation(@RequestParam String id) {
	    ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	    goodsStorageLocationService.deleteById(id);
	   return resBody;
	  }
}
