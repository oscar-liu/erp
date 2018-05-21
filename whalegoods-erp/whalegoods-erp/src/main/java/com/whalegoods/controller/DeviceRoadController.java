package com.whalegoods.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.Device;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.ReType;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.StringUtil;


/**
 * 设备相关接口
 * @author henrysun
 * 2018年5月8日 上午9:53:55
 */
@Controller
@RequestMapping(value = "/road")
public class DeviceRoadController  {
	
	  @Autowired
	  DeviceRoadService deviceRoadService; 
	  
	  @Autowired
	  DeviceService deviceService; 
	  
	  @Autowired
	  GoodsAdsTopService goodsAdsTopService; 
	  
	  @Autowired
	  GoodsAdsMiddleService goodsAdsMiddleService;
	  
	  @Autowired
	  GoodsSkuService goodsSkuService;
	  
	  @Autowired
	  PayService payService;

	  /**
	   * 跳转到货道列表页面
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showRoad")
	  @RequiresPermissions("device:road:list")
	  public String showRoad(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/device/road/roadList";
	  }

	  /**
	   * 查询货道列表接口
	   * @author henrysun
	   * 2018年5月7日 上午10:12:34
	   */
	  @GetMapping(value = "showRoadList")
	  @ResponseBody
	  @RequiresPermissions("device:road:list")
	  public ReType showRoadList(Model model, DeviceRoad deviceRoad , String page, String limit) {
		  return deviceRoadService.selectByPage(deviceRoad,Integer.valueOf(page),Integer.valueOf(limit));
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

	  /**
	   * 添加货道接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "addRoad")
	  @ResponseBody
	  public ResBody addRoad(@RequestBody DeviceRoad deviceRoad) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(deviceRoad.getWarningNum()>=deviceRoad.getCapacity())
		  {
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  //查询设备是否存在
		  Device device=deviceService.selectById(deviceRoad.getDeviceId());
		  if(device==null)
		  {
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",deviceRoad.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null) 
		  {
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  //查询货道是否已存在
		  Map<String,Object> mapCdt2=new HashMap<>();
		  mapCdt2.put("deviceId",deviceRoad.getDeviceId());
		  mapCdt2.put("ctn",deviceRoad.getCtn());
		  mapCdt2.put("floor",deviceRoad.getFloor());
		  mapCdt2.put("pathCode",deviceRoad.getPathCode());
		  if(deviceRoadService.selectCountByMapCdt(mapCdt2)>0){
			  throw new BizApiException(ConstApiResCode.PATH_EXIST);
		  }
		  deviceRoad.setId(StringUtil.getUUID());
		  deviceRoad.setGoodsSkuId(goodsSku.getId());
		  deviceRoad.setDeviceId(device.getId());
		  deviceRoad.setStock(0);
		  deviceRoad.setCreateBy(ShiroUtil.getCurrentUserId());
		  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		  deviceRoadService.insert(deviceRoad);
		  return resBody;
	  }

	  /**
	   * 跳转到更新货道信息页面
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateRoad")
	  public String showUpdateRoad(@RequestParam String id, Model model) {
		DeviceRoad deviceRoad= deviceRoadService.selectById(id);
		model.addAttribute("road",deviceRoad);
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/device/road/update-road";
	  }


	  /**
	   * 更新货道信息接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateRoad")
	  @ResponseBody
	  public ResBody updateRoad(@RequestBody DeviceRoad deviceRoad) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(deviceRoad.getWarningNum()>=deviceRoad.getCapacity())
		  {
			  throw new BizApiException(ConstApiResCode.CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM);
		  }
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",deviceRoad.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null)
		  {
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  //查询货道是否已存在
		  Map<String,Object> mapCdt2=new HashMap<>();
		  mapCdt2.put("deviceIdJp",deviceRoad.getDeviceIdJp());
		  mapCdt2.put("ctn",deviceRoad.getCtn());
		  mapCdt2.put("floor",deviceRoad.getFloor());
		  mapCdt2.put("pathCode",deviceRoad.getPathCode());
		  if(deviceRoadService.selectCountByMapCdt(mapCdt2)>0){
			  deviceRoad.setCtn(null);
			  deviceRoad.setFloor(null);
			  deviceRoad.setPathCode(null);
		  }
		  if(deviceRoad.getLockStatus()==1)
		  {
			  deviceRoad.setStock(null);
		  }
		  deviceRoad.setGoodsSkuId(goodsSku.getId());
		  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		  deviceRoadService.updateByObjCdtForErp(deviceRoad);
		  return resBody;
	  }

	  /**
	   * 删除货道接口
	   * @author henrysun
	   * 2018年4月26日 下午3:32:39
	   */
	  @PostMapping(value = "delRoad")
	  @ResponseBody
	  @RequiresPermissions("device:road:del")
	  public ResBody delRoad(@RequestParam String id) {
       ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
       deviceRoadService.deleteById(id);
	   return resBody;
	  }
	  
		/**
		 * 生成预支付订单（ERP）
		 * @author henrysun
		 * 2018年5月13日 下午4:17:03
		 */
	  @SuppressWarnings({ "deprecation", "rawtypes" })
	  @GetMapping(value="/createPrepayBack")
	  @RequiresPermissions("device:road:prepay")
	  String createPrepay(ReqCreatePrepay reqCreatePrepay,Model model) throws SystemException {
		  String order=((Map)payService.createPrepay(reqCreatePrepay,(byte) 2).getData()).get("order").toString();
		  ReqCreateQrCode reqObj=new ReqCreateQrCode();
		  reqObj.setOrder(order);
		  reqObj.setPayType(reqCreatePrepay.getSaleType());
		  String qrcodeUrl=((Map)payService.getQrCode(reqObj).getData()).get("qrcode_url").toString();
		  String newQrCodeUrl=new String(Base64Utils.decodeFromString( URLDecoder.decode(qrcodeUrl)));
	      model.addAttribute("qrCodeUrl",URLEncoder.encode(newQrCodeUrl));
		  return "/device/road/qrcode";
		}
	
}
