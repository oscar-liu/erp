package com.whalegoods.controller;

import java.util.List;

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

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.GoodsStorage;
import com.whalegoods.entity.GoodsStorageIn;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.GoodsStorageInService;
import com.whalegoods.service.GoodsStorageService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;

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
	 GoodsStorageService goodsStorageService;
	 
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
	   * 跳转到添加商品入库页面
	   * @author henrysun
	   * 2018年8月29日 下午2:29:32
	   */
	  @GetMapping(value = "showAddGoodsStorageIn")
	  public String showAddGoodsStorageIn(Model model) {
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	   return "/storage/in/add-goodsStorageIn";
	  }

	  /**
	   * 添加商品入库接口
	   * @author henrysun
	   * 2018年8月29日 下午2:30:39
	   */
	  @PostMapping(value = "addGoodsStorageIn")
	  @ResponseBody
	  public ResBody addGoodsStorageIn(@RequestBody GoodsStorageIn goodsStorageIn) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(goodsStorageIn.getCostPrice()>goodsStorageIn.getMarketPrice()){
			  throw new BizApiException(ConstApiResCode.COST_CANNOT_BIGGER_THAN_MARKET);
		  }
		  if(goodsStorageIn.getExpiringDate().before(goodsStorageIn.getProductDate())){
			  throw new BizApiException(ConstApiResCode.PRODUCTION_DATE_AFTER_EXPIRING_DATE);
		  }
		  //如果库存表没有该商品该到期日期的库存记录，则新增一条，否则更新库存
		  GoodsStorage objCdt=new GoodsStorage();
		  objCdt.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
		  objCdt.setExpiringDate(goodsStorageIn.getExpiringDate());
		  objCdt.setCurrCount(goodsStorageIn.getInCount());
		  List<GoodsStorage> liStorage=goodsStorageService.selectListByObjCdt(objCdt);
		  if(liStorage.size()>0){
			  goodsStorageService.updateByObjCdt(objCdt);
		  }
		  else{
			  objCdt.setId(StringUtil.getUUID());
			  objCdt.setCreateBy(ShiroUtil.getCurrentUserId());
			  objCdt.setUpdateBy(ShiroUtil.getCurrentUserId());
			  goodsStorageService.insert(objCdt);
		  }
		  goodsStorageIn.setId(StringUtil.getUUID());
		  goodsStorageIn.setCreateBy(ShiroUtil.getCurrentUserId());
		  goodsStorageIn.setUpdateBy(ShiroUtil.getCurrentUserId());
		  goodsStorageInService.insert(goodsStorageIn);
		  return resBody;
	  }
	  
	  /**
	   * 删除商品入库记录
	   * @author henrysun
	   * 2018年8月29日 下午2:31:26
	   */
	  @PostMapping(value = "delGoodsStorageIn")
	  @ResponseBody
	  @RequiresPermissions("storage:in:del")
	  public ResBody delGoodsStorageIn(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(id);
       if(goodsStorageIn!=null){
    	   goodsStorageInService.deleteById(id);
    	   GoodsStorage objCdt=new GoodsStorage();
    	   objCdt.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
    	   objCdt.setCurrCount(-goodsStorageIn.getInCount());
    	   objCdt.setExpiringDate(goodsStorageIn.getExpiringDate());
    	   goodsStorageService.updateByObjCdt(objCdt);
       }
	   return resBody;
	  }
	  
	  /**
	   * 跳转到仓库库存查询页面
	   * @author henrysun
	   * 2018年8月29日 下午3:01:45
	   */
	  @GetMapping(value = "showGoodsStorage")
	  @RequiresPermissions("storage:list")
	  public String showGoodsStorage(Model model) {
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/storage/storageList";
	  }

	  /**
	   * 查询仓库库存接口
	   * @author henrysun
	   * 2018年8月29日 下午3:02:15
	   */
	  @GetMapping(value = "showGoodsStorageList")
	  @ResponseBody
	  @RequiresPermissions("storage:list")
	  public ReType showGoodsStorageList(Model model, GoodsStorage goodsStorage , String page, String limit) {
		  return goodsStorageService.selectByPage(goodsStorage,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	
}
