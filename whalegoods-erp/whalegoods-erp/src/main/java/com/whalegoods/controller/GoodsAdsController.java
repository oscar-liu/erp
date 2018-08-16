package com.whalegoods.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.FileUtil;
import com.whalegoods.util.JsonUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;


/**
 * 后台促销管理相关接口
 * @author henrysun
 * 2018年5月14日 下午3:18:28
 */
@Controller
@RequestMapping(value = "/ads")
public class GoodsAdsController  {
	
	  @Autowired
	  GoodsAdsMiddleService adsMiddleService; 
	  
	  @Autowired
	  GoodsAdsTopService adsTopService; 

	  @Autowired
	  DeviceService deviceService; 
	  
	  @Autowired
	  GoodsSkuService goodsSkuService;
	  
	  @Autowired
	  DeviceRoadService deviceRoadService;
	  
	  @Autowired
	  FileUtil fileUtil;
	  
	  /**
	   * 跳转到促销列表
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showAdsMiddle")
	  @RequiresPermissions("ads:middle:list")
	  public String showAdsMiddle(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/ads/middle/middleList";
	  }

	  /**
	   * 查询促销列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showAdsMiddleList")
	  @ResponseBody
	  @RequiresPermissions("ads:middle:list")
	  public ReType showAdsMiddleList(Model model, GoodsAdsMiddle goodsAdsMiddle, String page, String limit) {
		Page<GoodsAdsMiddle> tPage= PageHelper.startPage(Integer.valueOf(page),Integer.valueOf(limit));
	    List<GoodsAdsMiddle> goodsAdsMiddles=adsMiddleService.selectListByObjCdt(goodsAdsMiddle);
	    for (GoodsAdsMiddle item : goodsAdsMiddles) {
	    	//整点
			if(item.getType()==1){
				item.setTimeRange(item.getStartHms()+"至"+item.getEndHms());
			}
			//时间段
/*			if(item.getType()==2){
				item.setTimeRange(DateUtil.formatDateTime(item.getStartDate())+"至"+DateUtil.formatDateTime(item.getEndDate()));
			}*/
		}
	   ReType reType=new ReType(tPage.getTotal(),goodsAdsMiddles);
	   return reType;
	  }
	  
	  /**
	   * 跳转到添加促销商品页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddAdsMiddle")
	  public String showAddAdsMiddle(Model model) {
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/ads/middle/add-adsMiddle";
	  }
	  
	  /**
	   * 添加促销商品接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	 * @throws SystemException 
	   */
	  @PostMapping(value = "addAdsMiddle")
	  @ResponseBody
	  public ResBody addAdsMiddle(@RequestBody String adsMiddles) throws SystemException {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  List<GoodsAdsMiddle> list = JSON.parseArray(adsMiddles, GoodsAdsMiddle.class);
		  //无论是整点还是时间段，时间范围都必须其中一个不为空
		  if(StringUtil.isEmpty(list.get(0).getHmsRange())&&StringUtil.isEmpty(list.get(0).getDateRange())){
			  throw new BizApiException(ConstApiResCode.TIME_RANGE_NOT_EMPTY);
		  }	  
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("deviceId", list.get(0).getDeviceId());
		  mapCdt.put("type", list.get(0).getType());
		  //校验是否和当前设备其他促销时间段有重叠
		  Date nowDate=new Date();
		  List<GoodsAdsMiddle> adsMiddles2=adsMiddleService.selectTimeRangeByDeviceId(mapCdt);
		  for (GoodsAdsMiddle item : adsMiddles2){
			  //整点
			  if(list.get(0).getType()==1){
				  if(DateUtil.isOverlap(DateUtil.getFormatHms(item.getStartHms(),nowDate), DateUtil.getFormatHms(item.getStartHms(),nowDate), DateUtil.getFormatHms(list.get(0).getStartHms(),nowDate),  DateUtil.getFormatHms(list.get(0).getEndHms(),nowDate))){
					  throw new BizApiException(ConstApiResCode.IS_OVER_LAP);
				  }  
			  }
			  //时间段
/*			  if(goodsAdsMiddle.getType()==2){
				  
			  }*/
		  }
		  adsMiddles2=new ArrayList<>();
		  for (GoodsAdsMiddle item : list){
			  item.setId(StringUtil.getUUID());
			  item.setCreateBy(ShiroUtil.getCurrentUserId());
			  item.setUpdateBy(ShiroUtil.getCurrentUserId());
			  adsMiddles2.add(item);
		  }
		  adsMiddleService.insertBatch(adsMiddles2);
		  return resBody;
	  }
	  
	  /**
	   * 获取商品参考促销价
	   */
	  @GetMapping(value = "getRecommendSalePrice")
	  @ResponseBody
	  public ResBody getRecommendSalePrice(@RequestParam String goodsCode, Model model) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		List<Double> listSalePrice=adsMiddleService.selectLastSalePrice(goodsCode);
		StringBuffer sb=new StringBuffer();
		if(listSalePrice.size()==0){
			sb.append("无,");
		}
		else{
			for (Double item : listSalePrice) {
				sb.append(item.toString()+"元");
				sb.append(",");
			}
		}
		resBody.setData(sb.toString().substring(0,sb.toString().length()-1));
		return resBody;
	  }
	  
	  /**
	   * 跳出设置促销商品的弹出框
	   */
	  @GetMapping(value = "showUpdateMiddleAdsGoods")
	  public String showUpdateMiddleAdsGoods(@RequestParam String id, Model model) {
		GoodsAdsMiddle goodsAdsMiddle= adsMiddleService.selectById(id);
		model.addAttribute("goodsAdsMiddle",goodsAdsMiddle);
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/ads/middle/set-goods";
	  }
	  
	  /**
	   * 设置促销商品接口
	   * @author henrysun
	   * 2018年8月6日 下午3:30:49
	   */
	  @PostMapping(value = "updateMiddleAdsGoods")
	  @ResponseBody
	  public ResBody updateMiddleAdsGoods(@RequestBody GoodsAdsMiddle goodsAdsMiddle) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",goodsAdsMiddle.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  //判断该设备相同的时间段是否已存在相同的商品
		  mapCdt.put("deviceId",goodsAdsMiddle.getDeviceId());
		  mapCdt.put("startHms",goodsAdsMiddle.getStartHms());
		  mapCdt.put("endHms",goodsAdsMiddle.getEndHms());
		  mapCdt.put("id",goodsAdsMiddle.getId());
		  if(adsMiddleService.selectCountByMapCdt(mapCdt)>0){
			  throw new BizApiException(ConstApiResCode.SAME_TIME_HAS_SAME_GOODS);
		  }
		  //促销价不能大于等于原价
		  DeviceRoad objCdt=new DeviceRoad();
		  objCdt.setDeviceId(goodsAdsMiddle.getDeviceId());
		  objCdt.setGoodsSkuId(goodsSku.getId());
		  List<DeviceRoad> listDeviceRoad=deviceRoadService.selectListByObjCdt(objCdt);
		  if(listDeviceRoad.size()>0&&listDeviceRoad.get(0).getSalePrice()!=null){
			  if(goodsAdsMiddle.getSalePrice()>=listDeviceRoad.get(0).getSalePrice()){
				  throw new BizApiException(ConstApiResCode.SALE_PRICE_CANNOT_BIGGER_OR_EQUALS_MARKET_PRICE);
			  }
		  }
		  //更新促销商品 
		  goodsAdsMiddle.setUpdateBy(ShiroUtil.getCurrentUserId());
		  adsMiddleService.updateByObjCdt(goodsAdsMiddle);
		  //保持相同设备相同商品促销价一致
		  GoodsAdsMiddle objCdt2=new GoodsAdsMiddle();
		  objCdt2.setSalePrice(goodsAdsMiddle.getSalePrice());
		  objCdt2.setDeviceId(goodsAdsMiddle.getDeviceId());
		  objCdt2.setGoodsCode(goodsAdsMiddle.getGoodsCode());
		  adsMiddleService.updateKeepSamePrice(objCdt2);
		  return resBody;
	  }

	  /**
	   * 删除促销商品接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "delAdsMiddle")
	  @ResponseBody
	  @RequiresPermissions("ads:middle:del") 
	  public ResBody delAdsMiddle(String[] ids) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       for (String id : ids){
    	   adsMiddleService.deleteById(id);
       }
	   return resBody;
	  }
	  
	  /**
	   * 跳转到广告列表
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showAdsTop")
	  @RequiresPermissions("ads:top:list")
	  public String showAdsTop(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/ads/top/topList";
	  }

	  /**
	   * 查询广告列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showAdsTopList")
	  @ResponseBody
	  @RequiresPermissions("ads:top:list")
	  public ReType showAdsTopList(Model model, GoodsAdsTop goodsAdsTop, String page, String limit) {
	    return adsTopService.selectByPage(goodsAdsTop,Integer.valueOf(page),Integer.valueOf(limit));
	  }
	  
	  /**
	   * 跳转到添加广告页面
	   * @author henrysun
	   * 2018年5月8日 上午10:09:50
	   */
	  @GetMapping(value = "showAddAdsTop")
	  public String showAddAdsTop(Model model) {
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	   model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/ads/top/add-adsTop";
	  }
	  
	  /**
	   * 添加广告接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "addAdsTop")
	  @ResponseBody
	  public ResBody addAdsTop(@RequestBody GoodsAdsTop goodsAdsTop) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(goodsAdsTop.getActionType()==1){
			  goodsAdsTop.setGoodsCode(StringUtil.randomString(3));
		  }
		  if(goodsAdsTop.getActionType()==2&&StringUtil.isEmpty(goodsAdsTop.getGoodsCode())){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EMPTY);
		  }
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("deviceId", goodsAdsTop.getDeviceId());
/*		  if(adsTopService.selectCountByMapCdt(mapCdt)>=3)
		  {
			  throw new BizApiException(ConstApiResCode.ADS_TOP_ALREADY_THREE);
		  }*/
		  mapCdt.put("goodsCode", goodsAdsTop.getGoodsCode());
		  mapCdt.put("actionType",2);
		  if(adsTopService.selectCountByMapCdt(mapCdt)>=1)
		  {
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_FOR_ADS_TOP_EXIST);
		  }
		  goodsAdsTop.setId(StringUtil.getUUID());
		  goodsAdsTop.setCreateBy(ShiroUtil.getCurrentUserId());
		  goodsAdsTop.setUpdateBy(ShiroUtil.getCurrentUserId());
		  adsTopService.insert(goodsAdsTop);
		  return resBody;
	  }
	  
	  /**
	   * 跳转到更新广告页面
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateAdsTop")
	  public String showUpdateAdsTop(@RequestParam String id, Model model) {
		GoodsAdsTop goodsAdsTop= adsTopService.selectById(id);
		model.addAttribute("adsTop",goodsAdsTop);
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/ads/top/update-adsTop";
	  }


	  /**
	   * 更新广告接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateAdsTop")
	  @ResponseBody
	  public ResBody updateAdsTop(@RequestBody GoodsAdsTop goodsAdsTop) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(goodsAdsTop.getActionType()==1){
			  goodsAdsTop.setGoodsCode(null);
		  }
		  if(goodsAdsTop.getActionType()==2&&StringUtil.isEmpty(goodsAdsTop.getGoodsCode())){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EMPTY);
		  }
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("deviceId", goodsAdsTop.getDeviceId());
		  mapCdt.put("goodsCode", goodsAdsTop.getGoodsCode());
		  if(goodsAdsTop.getActionType()==2&adsTopService.selectCountByMapCdt(mapCdt)>=1)
		  {
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_FOR_ADS_TOP_EXIST);
		  }
		  goodsAdsTop.setUpdateBy(ShiroUtil.getCurrentUserId());
		  adsTopService.updateByObjCdt(goodsAdsTop);
		  return resBody;
	  }

	  /**
	   * 删除广告接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "delAdsTop")
	  @ResponseBody
	  @RequiresPermissions("ads:top:del") 
	  public ResBody delAdsTop(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       adsTopService.deleteById(id);
	   return resBody;
	  }
	  
	  /**
	   * 上传顶部广告大图接口
	   * @author henrysun
	   * 2018年5月7日 上午11:42:14
	   */
	  @PostMapping(value = "uploadBigPic")
	  @ResponseBody
	  public JsonUtil uploadBigPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="big_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }
	  
	  /**
	   * 上传顶部广告缩略图接口
	   * @author henrysun
	   * 2018年5月7日 上午11:42:14
	   */
	  @PostMapping(value = "uploadTinyPic")
	  @ResponseBody
	  public JsonUtil uploadTinyPic(HttpServletRequest request,HttpSession session) {
		  JsonUtil jsonUtil=JsonUtil.sucess(null);
		  String childFolder="tiny_pic";
		  String newFileName=childFolder+"_"+System.currentTimeMillis()+StringUtil.randomString(3);
		  String fileUrl=null;
		  try {
			  fileUrl= fileUtil.uploadFile(request,childFolder,newFileName);
			  JSONObject json=new JSONObject();
			  json.put("file_url",fileUrl);
			  jsonUtil.setData(json);
		} catch (SystemException e) {
			jsonUtil.setFlag(false);
			jsonUtil.setMsg("上传失败，请联系管理员");
		}
		  return jsonUtil;
	  }

	
}
