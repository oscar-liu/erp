package com.whalegoods.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.whalegoods.entity.GoodsStorageRd;
import com.whalegoods.entity.GoodsStorageRtw;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.GoodsStorageInService;
import com.whalegoods.service.GoodsStorageLocationService;
import com.whalegoods.service.GoodsStorageOutService;
import com.whalegoods.service.GoodsStorageRdService;
import com.whalegoods.service.GoodsStorageRtwService;
import com.whalegoods.service.GoodsStorageService;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.FileUtil;
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
	 
	 @Autowired
	 GoodsStorageRdService goodsStorageRdService;
	 
	 @Autowired
	 GoodsStorageRtwService goodsStorageRtwService;

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
	   * 导出商品入库单
	   * @author henrysun
	   * 2018年9月18日 下午3:10:49
	   */
	  @GetMapping(value="/storageInExcel")
	  void storageInExcel(GoodsStorageIn goodsStorageIn,HttpServletResponse response) throws SystemException  {
			 if(!StringUtil.isEmpty(goodsStorageIn.getTimeRange())){
					String startExpiringDate=goodsStorageIn.getTimeRange().split(ConstSysParamName.KGANG)[0];
					String endExpiringDate=goodsStorageIn.getTimeRange().split(ConstSysParamName.KGANG)[1];
					goodsStorageIn.setStartExpiringDate(startExpiringDate);
					goodsStorageIn.setEndExpiringDate(endExpiringDate);
			 }
		  FileUtil.exportExcel(goodsStorageInService.selectListByObjCdt(goodsStorageIn),"商品入库清单","入库清单",GoodsStorageIn.class,"商品入库清单.xls",response);
		}
	  
	  /**
	   * 跳转到设置商品库位
	   * @author henrysun
	   * 2018年9月18日 下午3:56:54
	   */
	  @GetMapping(value = "showSetLocation")
	  public String showSetLocation(@RequestParam String id, Model model) {
		GoodsStorageIn goodsStorageIn= goodsStorageInService.selectById(id);
		model.addAttribute("goodsStorageIn",goodsStorageIn);
		model.addAttribute("locationList",goodsStorageLocationService.selectListByObjCdt(new GoodsStorageLocation()));
	    return "/storage/in/set-location";
	  }
	  
	  /**
	   * 设置商品库位接口
	   * @author henrysun
	   * 2018年9月18日 下午4:09:46
	   */
	  @PostMapping(value = "setLocation")
	  @ResponseBody
	  public ResBody setLocation(@RequestBody GoodsStorageIn goodsStorageIn) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		goodsStorageInService.updateByObjCdt(goodsStorageIn);
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
	    	   objCdt2.setCurrCount(goodsStorageOut.getApplyNum());
	    	   objCdt2.setExpiringDate(goodsStorageIn.getExpiringDate());
	 	       goodsStorageService.updateByObjCdt(objCdt2);
	       }
	       else{
	    	   throw new BizApiException(ConstApiResCode.GOODS_STORAGE_IN_NOT_EXIST);
	       }
       }
	   return resBody;
	  }
	  
	  /**
	   * 跳转到报损列表页面
	   * @author henrysun
	   * 2018年9月18日 下午6:38:13
	   */
	  @GetMapping(value = "showGoodsStorageRd")
	  @RequiresPermissions("storage:rd:list")
	  public String showGoodsStorageRd(Model model) {
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/storage/rd/storageRdList";
	  }

	  /**
	   * 查询报损列表接口
	   * @author henrysun
	   * 2018年8月30日 上午10:19:57
	   */
	  @GetMapping(value = "showGoodsStorageRdList")
	  @ResponseBody
	  @RequiresPermissions("storage:rd:list")
	  public ReType showGoodsStorageRdList(Model model, GoodsStorageRd goodsStorageRd , String page, String limit) {
		 return goodsStorageRdService.selectByPage(goodsStorageRd,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加商品报损页面
	   * @author henrysun
	   * 2018年9月19日 下午3:12:19
	   */
	  @GetMapping(value = "showAddGoodsStorageRd")
	  public String showAddGoodsStorageRd(Model model) {
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	   return "/storage/rd/add-goodsStorageRd";
	  }
	  
	  /**
	   * 添加商品报损接口
	   * @author henrysun
	   * 2018年9月19日 下午3:12:45
	   */
	  @PostMapping(value = "addGoodsStorageRd")
	  @ResponseBody
	  public ResBody addGoodsStorageRd(@RequestBody GoodsStorageRd goodsStorageRd) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageRd.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	 		  if(goodsStorageRd.getRdDay().before(goodsStorageIn.getInDate())){
				  throw new BizApiException(ConstApiResCode.RD_DATE_CANNOT_BEFORE_IN_DATE);
			  }
	    	   if(goodsStorageRd.getRdNum()>goodsStorageIn.getCurrCount()){
	    		   resBody.setResultMsg("当前入库批次最大可报损数量为："+goodsStorageIn.getCurrCount());
	    		   resBody.setResultCode(ConstApiResCode.CURR_STORAGE_RD_INAVALIBLE_COUNT);
	    		   return resBody;
	    	   }
	    	   else{
	    		   goodsStorageRd.setId(StringUtil.getUUID());
	    		   goodsStorageRd.setCreateBy(ShiroUtil.getCurrentUserId());
	    		   goodsStorageRd.setUpdateBy(ShiroUtil.getCurrentUserId());
	    		   goodsStorageRdService.insert(goodsStorageRd);
	    			  //扣除当前入库批次的库存
	    			  goodsStorageIn.setCurrCount(-goodsStorageRd.getRdNum());
	    			  goodsStorageInService.updateByObjCdt(goodsStorageIn);
	    			  //扣除库存表的总库存
	   	    	GoodsStorage objCdt2=new GoodsStorage();
	   	    	objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	   	    	objCdt2.setCurrCount(-goodsStorageRd.getRdNum());
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
	   * 删除商品报损记录
	   * @author henrysun
	   * 2018年9月19日 下午4:22:47
	   */
	  @PostMapping(value = "delGoodsStorageRd")
	  @ResponseBody
	  @RequiresPermissions("storage:rd:del")
	  public ResBody delGoodsStorageRd(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       GoodsStorageRd goodsStorageRd=goodsStorageRdService.selectById(id);
       if(goodsStorageRd!=null){
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageRd.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	    	   goodsStorageRdService.deleteById(id);
				  //增加当前入库批次的库存
				  goodsStorageIn.setCurrCount(goodsStorageRd.getRdNum());
				  goodsStorageInService.updateByObjCdt(goodsStorageIn);
				  //增加库存表的总库存
	    	   GoodsStorage objCdt2=new GoodsStorage();
	    	   objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	    	   objCdt2.setCurrCount(goodsStorageRd.getRdNum());
	    	   objCdt2.setExpiringDate(goodsStorageIn.getExpiringDate());
	 	       goodsStorageService.updateByObjCdt(objCdt2);
	       }
	       else{
	    	   throw new BizApiException(ConstApiResCode.GOODS_STORAGE_IN_NOT_EXIST);
	       }
       }
	   return resBody;
	  }
	  
	  /**
	   * 跳转到返仓列表页面
	   * @author henrysun
	   * 2018年9月20日 下午9:31:53
	   */
	  @GetMapping(value = "showGoodsStorageRtw")
	  @RequiresPermissions("storage:rtw:list")
	  public String showGoodsStorageRtw(Model model) {
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/storage/rtw/storageRtwList";
	  }

	  /**
	   * 查询返仓列表接口
	   * @author henrysun
	   * 2018年9月20日 下午9:32:00
	   */
	  @GetMapping(value = "showGoodsStorageRtwList")
	  @ResponseBody
	  @RequiresPermissions("storage:rtw:list")
	  public ReType showGoodsStorageRtwList(Model model, GoodsStorageRtw goodsStorageRtw , String page, String limit) {
		 return goodsStorageRtwService.selectByPage(goodsStorageRtw,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加商品返仓页面
	   * @author henrysun
	   * 2018年9月21日 上午4:36:05
	   */
	  @GetMapping(value = "showAddGoodsStorageRtw")
	  public String showAddGoodsStorageRtw(Model model) {
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	   model.addAttribute("goodsStorageInList",goodsStorageInService.selectListByObjCdt(new GoodsStorageIn()));
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	   return "/storage/rtw/add-goodsStorageRtw";
	  }
	  
	  /**
	   * 添加商品返仓接口
	   * @author henrysun
	   * 2018年9月21日 上午4:35:51
	   */
	  @PostMapping(value = "addGoodsStorageRtw")
	  @ResponseBody
	  public ResBody addGoodsStorageRtw(@RequestBody GoodsStorageRd goodsStorageRd) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageRd.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	 		  if(goodsStorageRd.getRdDay().before(goodsStorageIn.getInDate())){
				  throw new BizApiException(ConstApiResCode.RD_DATE_CANNOT_BEFORE_IN_DATE);
			  }
	    	   if(goodsStorageRd.getRdNum()>goodsStorageIn.getCurrCount()){
	    		   resBody.setResultMsg("当前入库批次最大可报损数量为："+goodsStorageIn.getCurrCount());
	    		   resBody.setResultCode(ConstApiResCode.CURR_STORAGE_RD_INAVALIBLE_COUNT);
	    		   return resBody;
	    	   }
	    	   else{
	    		   goodsStorageRd.setId(StringUtil.getUUID());
	    		   goodsStorageRd.setCreateBy(ShiroUtil.getCurrentUserId());
	    		   goodsStorageRd.setUpdateBy(ShiroUtil.getCurrentUserId());
	    		   goodsStorageRdService.insert(goodsStorageRd);
	    			  //扣除当前入库批次的库存
	    			  goodsStorageIn.setCurrCount(-goodsStorageRd.getRdNum());
	    			  goodsStorageInService.updateByObjCdt(goodsStorageIn);
	    			  //扣除库存表的总库存
	   	    	GoodsStorage objCdt2=new GoodsStorage();
	   	    	objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	   	    	objCdt2.setCurrCount(-goodsStorageRd.getRdNum());
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
	   * 删除商品返仓记录
	   * @author henrysun
	   * 2018年9月21日 上午4:35:35
	   */
	  @PostMapping(value = "delGoodsStorageRtw")
	  @ResponseBody
	  @RequiresPermissions("storage:rtw:del")
	  public ResBody delGoodsStorageRtw(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       GoodsStorageRd goodsStorageRd=goodsStorageRdService.selectById(id);
       if(goodsStorageRd!=null){
	       GoodsStorageIn goodsStorageIn=goodsStorageInService.selectById(goodsStorageRd.getGoodsStorageInId());
	       if(goodsStorageIn!=null){
	    	   goodsStorageRdService.deleteById(id);
				  //增加当前入库批次的库存
				  goodsStorageIn.setCurrCount(goodsStorageRd.getRdNum());
				  goodsStorageInService.updateByObjCdt(goodsStorageIn);
				  //增加库存表的总库存
	    	   GoodsStorage objCdt2=new GoodsStorage();
	    	   objCdt2.setGoodsSkuId(goodsStorageIn.getGoodsSkuId());
	    	   objCdt2.setCurrCount(goodsStorageRd.getRdNum());
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
