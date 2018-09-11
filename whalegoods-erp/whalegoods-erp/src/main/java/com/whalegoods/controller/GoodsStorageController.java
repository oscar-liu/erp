package com.whalegoods.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.GoodsStorage;
import com.whalegoods.entity.GoodsStorageIn;
import com.whalegoods.entity.GoodsStorageLocation;
import com.whalegoods.entity.GoodsStorageOut;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.GoodsStorageInService;
import com.whalegoods.service.GoodsStorageLocationService;
import com.whalegoods.service.GoodsStorageOutService;
import com.whalegoods.service.GoodsStorageService;
import com.whalegoods.util.DateUtil;
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
	 GoodsStorageOutService goodsStorageOutService; 
	 
	 @Autowired
	 GoodsStorageService goodsStorageService;
	 
	 @Autowired
	 DeviceService deviceService;
	 
	 @Autowired
	 GoodsSkuService goodsSkuService;
	 
	 @Autowired
	 GoodsStorageLocationService goodsStorageLocationService;

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
			 if(!StringUtil.isEmpty(goodsStorageIn.getTimeRange())){
					String startExpiringDate=goodsStorageIn.getTimeRange().split(ConstSysParamName.KGANG)[0];
					String endExpiringDate=goodsStorageIn.getTimeRange().split(ConstSysParamName.KGANG)[1];
					goodsStorageIn.setStartExpiringDate(startExpiringDate);
					goodsStorageIn.setEndExpiringDate(endExpiringDate);
			 }
		  return goodsStorageInService.selectByPage(goodsStorageIn,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加商品入库页面
	   * @author henrysun
	   * 2018年8月29日 下午2:29:32
	   */
	  @GetMapping(value = "showAddGoodsStorageIn")
	  public String showAddGoodsStorageIn(Model model) {
	   model.addAttribute("inId","WGRK"+System.currentTimeMillis());
	   model.addAttribute("locationList",goodsStorageLocationService.selectListByObjCdt(new GoodsStorageLocation()));
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
		  goodsStorageIn.setCurrCount(goodsStorageIn.getInCount());
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
    	   objCdt.setCurrCount(-goodsStorageIn.getCurrCount());
    	   objCdt.setExpiringDate(goodsStorageIn.getExpiringDate());
    	   goodsStorageService.updateByObjCdt(objCdt);
       }
	   return resBody;
	  }
	  
	  /**
	   * 根据商品编号查询过期日期
	   * @author henrysun
	   * 2018年9月7日 上午10:50:15
	 * @throws SystemException 
	   */
	  @GetMapping(value = "getExpiringDateByGoodsSkuId")
	  @ResponseBody
	  public ResBody getExpiringDateByGoodsSkuId(Model model,@RequestParam String goodsSkuId,@RequestParam String productDate) throws SystemException {
	   ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	   Map<String,Object> mapCdt=new HashMap<>();
	   mapCdt.put("goodsSkuId",goodsSkuId);
	   GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
	   if(goodsSku==null){
		   throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
	   }
	   else{
		   Calendar calendar = new GregorianCalendar();
		   @SuppressWarnings("static-access")
		   String expiringDate=DateUtil.getMoveDate(DateUtil.stringToDateYmd(productDate),calendar.DATE,goodsSku.getShelfLife()*30);
		   resBody.setData(expiringDate);
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
	  
	  /**
	   * 跳转到仓库出库列表页面
	   * @author henrysun
	   * 2018年8月30日 上午10:18:40
	   */
	  @GetMapping(value = "showGoodsStorageOut")
	  @RequiresPermissions("storage:out:list")
	  public String showGoodsStorageOut(Model model) {
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/storage/s_out/storageOutList";
	  }

	  /**
	   * 查询仓库出库列表接口
	   * @author henrysun
	   * 2018年8月30日 上午10:19:57
	   */
	  @GetMapping(value = "showGoodsStorageOutList")
	  @ResponseBody
	  @RequiresPermissions("storage:out:list")
	  public ReType showGoodsStorageOutList(Model model, GoodsStorageOut goodsStorageOut , String page, String limit) {
		 if(!StringUtil.isEmpty(goodsStorageOut.getTimeRange())){
				String startApplyDate=goodsStorageOut.getTimeRange().split(ConstSysParamName.KGANG)[0];
				String endApplyDate=goodsStorageOut.getTimeRange().split(ConstSysParamName.KGANG)[1];
				goodsStorageOut.setStartApplyDate(startApplyDate);
				goodsStorageOut.setEndApplyDate(endApplyDate);
		 }
		 return goodsStorageOutService.selectByPage(goodsStorageOut,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加商品出库页面
	   * @author henrysun
	   * 2018年8月30日 上午10:21:58
	   */
	  @GetMapping(value = "showAddGoodsStorageOut")
	  public String showAddGoodsStorageOut(Model model) {
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	   return "/storage/s_out/add-goodsStorageOut";
	  }
	  
	  /**
	   * 根据商品编号查询对应的入库批次
	   * @author henrysun
	   * 2018年8月30日 下午5:15:11
	   */
	  @GetMapping(value = "getStorageInListByGoodsSkuId")
	  @ResponseBody
	  public ResBody getStorageInListByGoodsSkuId(Model model,@RequestParam String goodsSkuId) {
	   ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	   List<GoodsStorageIn> liGoodsStorageIn=goodsStorageInService.getStorageInListByGoodsSkuId(goodsSkuId);
	   if(liGoodsStorageIn.size()>0){
		   resBody.setData(liGoodsStorageIn);
	   }
	   else{
		   throw new BizApiException(ConstApiResCode.NO_AVALIBALE_GOODS_STORAGE_IN);
	   }
	   return resBody;
	  }
	  
	  /**
	   * 添加商品出库接口
	   * @author henrysun
	   * 2018年9月2日 下午8:08:31
	   */
	  @PostMapping(value = "addGoodsStorageOut")
	  @ResponseBody
	  public ResBody addGoodsStorageOut(@RequestBody GoodsStorageOut goodsStorageOut) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageOut.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	 		  if(goodsStorageOut.getApplyDate().before(goodsStorageIn.getInDate())){
				  throw new BizApiException(ConstApiResCode.OUT_DATE_CANNOT_BEFORE_IN_DATE);
			  }
	    	   if(goodsStorageOut.getApplyNum()>goodsStorageIn.getCurrCount()){
	    		   resBody.setResultMsg("当前入库批次最大可出库数量为："+goodsStorageIn.getCurrCount());
	    		   resBody.setResultCode(ConstApiResCode.CURR_STORAGE_IN_INAVALIBLE_COUNT);
	    		   return resBody;
	    	   }
	    	   else{
	    			  goodsStorageOut.setId(StringUtil.getUUID());
	    			  goodsStorageOut.setApplyBy(ShiroUtil.getCurrentUserId());
	    			  goodsStorageOut.setCreateBy(ShiroUtil.getCurrentUserId());
	    			  goodsStorageOut.setUpdateBy(ShiroUtil.getCurrentUserId());
	    			  goodsStorageOutService.insert(goodsStorageOut);
	    			  //扣除当前入库批次的库存
	    			  goodsStorageIn.setCurrCount(-goodsStorageOut.getApplyNum());
	    			  goodsStorageInService.updateByObjCdt(goodsStorageIn);
	    			  //扣除库存表的总库存
	   	    	GoodsStorage objCdt2=new GoodsStorage();
	   	    	objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	   	    	objCdt2.setCurrCount(-goodsStorageOut.getApplyNum());
	   	    	objCdt2.setExpiringDate(goodsStorageIn.getExpiringDate());
		    	goodsStorageService.updateByObjCdt(objCdt2);
	    	   }
	       }
	       else{
	    	   throw new BizApiException(ConstApiResCode.GOODS_STORAGE_IN_NOT_EXIST);
	       }

		  return resBody;
	  }

	  /**
	   * 删除商品出库记录
	   * @author henrysun
	   * 2018年9月2日 下午8:30:23
	   */
	  @PostMapping(value = "delGoodsStorageOut")
	  @ResponseBody
	  @RequiresPermissions("storage:out:del")
	  public ResBody delGoodsStorageOut(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       GoodsStorageOut goodsStorageOut=goodsStorageOutService.selectById(id);
       if(goodsStorageOut!=null){
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageOut.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	    	   goodsStorageOutService.deleteById(id);
				  //增加当前入库批次的库存
				  goodsStorageIn.setCurrCount(goodsStorageOut.getApplyNum());
				  goodsStorageInService.updateByObjCdt(goodsStorageIn);
				  //增加库存表的总库存
	    	   GoodsStorage objCdt2=new GoodsStorage();
	    	   objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	    	   objCdt2.setCurrCount(goodsStorageIn.getCurrCount());
	    	   objCdt2.setExpiringDate(goodsStorageIn.getExpiringDate());
	 	       goodsStorageService.updateByObjCdt(objCdt2);   
	       }
	       else{
	    	   throw new BizApiException(ConstApiResCode.GOODS_STORAGE_IN_NOT_EXIST);
	       }
       }
	   return resBody;
	  }
	
}
