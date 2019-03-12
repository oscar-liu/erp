package com.whalegoods.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.whalegoods.entity.DeviceModelStandard;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.GoodsSku;
import com.whalegoods.entity.GoodsStorageOut;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqInitRoad;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceModelStandardService;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.DeviceService;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.service.GoodsSkuService;
import com.whalegoods.service.GoodsStorageInService;
import com.whalegoods.service.GoodsStorageOutService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.FileUtil;
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
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	  
	  @Autowired
	  DeviceModelStandardService standardService;
	  
	  @Autowired
	  GoodsStorageInService goodsStorageInService;
	  
	  @Autowired
	  GoodsStorageOutService goodsStorageOutService; 

	  /**
	   * 跳转到货道列表页面
	   * @author henrysun
	   * 2018年5月7日 上午10:12:39
	   */
	  @GetMapping(value = "showRoad")
	  @RequiresPermissions("device:road:list")
	  public String showRoad(Model model) {
		model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
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
		  if(deviceRoad.getWarningNum()>=deviceRoad.getCapacity()){
			  throw new BizApiException(ConstApiResCode.CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM);
		  }
		  //查询设备是否存在
		  Device device=deviceService.selectById(deviceRoad.getDeviceId());
		  if(device==null){
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",deviceRoad.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null){
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
		  //原价不能小于等于促销价
		  GoodsAdsMiddle objCdt=new GoodsAdsMiddle();
		  objCdt.setDeviceId(deviceRoad.getDeviceId());
		  objCdt.setGoodsCode(deviceRoad.getGoodsCode());
		  List<GoodsAdsMiddle> listAdsMiddle=goodsAdsMiddleService.selectListByObjCdt(objCdt);
		  if(listAdsMiddle.size()>0){
			  listAdsMiddle.sort((GoodsAdsMiddle a,GoodsAdsMiddle b)->a.getSalePrice().compareTo(b.getSalePrice()));
			  if(deviceRoad.getSalePrice()<=listAdsMiddle.get(listAdsMiddle.size()-1).getSalePrice()){
				  throw new BizApiException(ConstApiResCode.MARKET_PRICE_CANNOT_SAMLLER_OR_EQUALS_SALE_PRICE);
			  }
		  }
		  deviceRoad.setId(StringUtil.getUUID());
		  deviceRoad.setGoodsSkuId(goodsSku.getId());
		  deviceRoad.setDeviceId(device.getId());
		  deviceRoad.setStock(0);
		  deviceRoad.setCreateBy(ShiroUtil.getCurrentUserId());
		  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		  deviceRoadService.insert(deviceRoad);
		  //将该设备相同的商品价格保持一致
		  Map<String,Object> mapCdt3=new HashMap<>();
		  mapCdt3.put("salePrice",deviceRoad.getSalePrice());
		  mapCdt3.put("deviceId",deviceRoad.getDeviceId());
		  mapCdt3.put("goodsSkuId",goodsSku.getId());
		  deviceRoadService.updateSalePrice(mapCdt3);
		  return resBody;
	  }
	  
	  /**
	   * 跳转到初始化货道页面
	   * @author henrysun
	   * 2018年5月29日 上午10:21:32
	   */
	  @GetMapping(value = "showInitRoad")
	  public String showInitRoad(Model model) {
	   model.addAttribute("deviceList",deviceService.selectListByObjCdt(new Device()));
	    return "/device/road/init-road";
	  }

	  /**
	   * 初始化货道接口
	   * @author henrysun
	   * 2018年5月7日 上午11:44:51
	   */
	  @PostMapping(value = "initRoad")
	  @ResponseBody
	  public ResBody initRoad(@RequestBody ReqInitRoad reqInitRoad) {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  if(reqInitRoad.getWarningNum()>=reqInitRoad.getCapacity())
		  {
			  throw new BizApiException(ConstApiResCode.CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM);
		  }
		  //删除该设备所有的货道
		  deviceRoadService.deleteByDeviceId(reqInitRoad.getDeviceId());
		  //查询设备是否存在
		  Device device=deviceService.selectById(reqInitRoad.getDeviceId());
		  if(device==null)
		  {
			  throw new BizApiException(ConstApiResCode.DEVICE_NOT_EXIST);
		  }
		  //查询型号标准
		  DeviceModelStandard standard=new DeviceModelStandard();
		  standard.setDeviceModelId(device.getDeviceModelId());
		  List<DeviceModelStandard> listStandard=standardService.selectListByObjCdt(standard);
		  if(!(listStandard.size()>0)){
			  throw new BizApiException(ConstApiResCode.STANDARD_NOT_EXIST);
		  }
		  List<DeviceRoad> listRoad=new ArrayList<>();
		  DeviceRoad deviceRoad=null;
		  for (DeviceModelStandard item : listStandard){
			  for(int i=1;i<=item.getPathNum();i++){
				  deviceRoad=new DeviceRoad();
				  deviceRoad.setId(StringUtil.getUUID());
				  deviceRoad.setCtn(item.getCtn());
				  deviceRoad.setFloor(item.getFloor());
				  deviceRoad.setPathCode((byte) i);
				  deviceRoad.setStock(0);
				  deviceRoad.setDeviceId(device.getId());
				  deviceRoad.setCapacity(reqInitRoad.getCapacity());
				  deviceRoad.setWarningNum(reqInitRoad.getWarningNum());
				  deviceRoad.setCreateBy(ShiroUtil.getCurrentUserId());
				  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
				  listRoad.add(deviceRoad);  
			  }
		  }
		  //批量添加货道
		  deviceRoadService.insertBatch(listRoad);
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
		  if(deviceRoad.getWarningNum()>=deviceRoad.getCapacity()){
			  throw new BizApiException(ConstApiResCode.CAPACITY_CANNOT_BIGGER_THAN_WARNINGNUM);
		  }
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",deviceRoad.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  //原价不能小于等于促销价
		  GoodsAdsMiddle objCdt=new GoodsAdsMiddle();
		  objCdt.setDeviceId(deviceRoad.getDeviceId());
		  objCdt.setGoodsCode(deviceRoad.getGoodsCode());
		  List<GoodsAdsMiddle> listAdsMiddle=goodsAdsMiddleService.selectListByObjCdt(objCdt);
		  if(listAdsMiddle.size()>0){
			  listAdsMiddle.sort((GoodsAdsMiddle a,GoodsAdsMiddle b)->a.getSalePrice().compareTo(b.getSalePrice()));
			  if(deviceRoad.getSalePrice()<=listAdsMiddle.get(listAdsMiddle.size()-1).getSalePrice()){
				  throw new BizApiException(ConstApiResCode.MARKET_PRICE_CANNOT_SAMLLER_OR_EQUALS_SALE_PRICE);
			  }
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
		  //将该设备相同的商品价格保持一致
		  Map<String,Object> mapCdt3=new HashMap<>();
		  mapCdt3.put("salePrice",deviceRoad.getSalePrice());
		  mapCdt3.put("deviceId",deviceRoad.getDeviceId());
		  mapCdt3.put("goodsSkuId",goodsSku.getId());
		  deviceRoadService.updateSalePrice(mapCdt3);
		  //重新绑定该商品最新的出库信息
		  String outId=goodsStorageOutService.selectTopOneOutIdByMapCdt(mapCdt3);
		  if(!StringUtil.isEmpty(outId)){
			  DeviceRoad objCdt2=new DeviceRoad();
			  objCdt2.setId(deviceRoad.getId());
			  objCdt2.setGoodsStorageOutId(outId);
			  deviceRoadService.updateByObjCdtForErp(objCdt2);
		  }
		  return resBody;
	  }
	  
	  /**
	   * 跳转到设置商品页面
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateGoods")
	  public String showUpdateGoods(@RequestParam String id, Model model) {
		DeviceRoad deviceRoad= deviceRoadService.selectById(id);
		model.addAttribute("road",deviceRoad);
		model.addAttribute("goodsList",goodsSkuService.selectListByObjCdt(new GoodsSku()));
	    return "/device/road/set-goods";
	  }

	  /**
	   * 更新货道商品接口
	   * @author henrysun
	   * 2018年4月26日 下午3:30:49
	   */
	  @PostMapping(value = "updateGoods")
	  @ResponseBody
	  public ResBody updateGoods(@RequestBody DeviceRoad deviceRoad) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  //查询商品是否存在
		  Map<String,Object> mapCdt=new HashMap<>();
		  mapCdt.put("goodsCode",deviceRoad.getGoodsCode());
		  GoodsSku goodsSku=goodsSkuService.selectByMapCdt(mapCdt);
		  if(goodsSku==null){
			  throw new BizApiException(ConstApiResCode.GOODS_CODE_NOT_EXIST);
		  }
		  //原价不能小于等于促销价
		  GoodsAdsMiddle objCdt=new GoodsAdsMiddle();
		  objCdt.setDeviceId(deviceRoad.getDeviceId());
		  objCdt.setGoodsCode(deviceRoad.getGoodsCode());
		  List<GoodsAdsMiddle> listAdsMiddle=goodsAdsMiddleService.selectListByObjCdt(objCdt);
		  if(listAdsMiddle.size()>0){
			  listAdsMiddle.sort((GoodsAdsMiddle a,GoodsAdsMiddle b)->a.getSalePrice().compareTo(b.getSalePrice()));
			  if(deviceRoad.getSalePrice()<=listAdsMiddle.get(listAdsMiddle.size()-1).getSalePrice()){
				  throw new BizApiException(ConstApiResCode.MARKET_PRICE_CANNOT_SAMLLER_OR_EQUALS_SALE_PRICE);
			  }
		  }
		  deviceRoad.setGoodsSkuId(goodsSku.getId());
		  deviceRoad.setUpdateBy(ShiroUtil.getCurrentUserId());
		  deviceRoadService.updateByObjCdtForErp(deviceRoad);
		  //将该设备相同的商品价格保持一致
		  Map<String,Object> mapCdt2=new HashMap<>();
		  mapCdt2.put("salePrice",deviceRoad.getSalePrice());
		  mapCdt2.put("deviceId",deviceRoad.getDeviceId());
		  mapCdt2.put("goodsSkuId",goodsSku.getId());
		  deviceRoadService.updateSalePrice(mapCdt2);
		  //重新绑定该商品最新的出库信息
		  String outId=goodsStorageOutService.selectTopOneOutIdByMapCdt(mapCdt2);
		  if(!StringUtil.isEmpty(outId)){
			  DeviceRoad objCdt2=new DeviceRoad();
			  objCdt2.setId(deviceRoad.getId());
			  objCdt2.setGoodsStorageOutId(outId);
			  deviceRoadService.updateByObjCdtForErp(objCdt2);
		  }
		  return resBody;
	  }
	  
	  /**
	   * 校验是否可以设置出库批次
	   * @author henrysun
	   * 2018年9月3日 上午11:52:28
	   */
	  @GetMapping(value = "validateCanSetGoodsStorageOut")
	  @ResponseBody
	  public ResBody validateCanSetGoodsStorageOut(@RequestParam String id, Model model) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		DeviceRoad deviceRoad= deviceRoadService.selectById(id);
		if(StringUtil.isEmpty(deviceRoad.getGoodsCode())){
			throw new BizApiException(ConstApiResCode.HAVE_TO_SET_GOODS);
		}
		GoodsStorageOut objCdt=new GoodsStorageOut();
		objCdt.setDeviceId(deviceRoad.getDeviceId());
		objCdt.setGoodsCode(deviceRoad.getGoodsCode());
		List<GoodsStorageOut> listGoodsStorageOut=goodsStorageOutService.selectListByObjCdtForSetDeviceRoad(objCdt);
		if(listGoodsStorageOut.size()==0){
			throw new BizApiException(ConstApiResCode.NO_AVALIBALE_GOODS_STORAGE_OUT);
		}
	    return resBody;
	  }
	  
	  /**
	   * 跳转到设置所属出库批次
	   * @author henrysun
	   * 2018年5月7日 上午11:54:54
	   */
	  @GetMapping(value = "showUpdateGoodsStorageOut")
	  public String showUpdateGoodsStorageOut(@RequestParam String id, Model model) {
		DeviceRoad deviceRoad= deviceRoadService.selectById(id);
		GoodsStorageOut objCdt=new GoodsStorageOut();
		objCdt.setDeviceId(deviceRoad.getDeviceId());
		objCdt.setGoodsCode(deviceRoad.getGoodsCode());
		model.addAttribute("road",deviceRoad);
		model.addAttribute("goodsStorageOutList",goodsStorageOutService.selectListByObjCdtForSetDeviceRoad(objCdt));
	    return "/device/road/set-goodsStorageOut";
	  }
	  
	  /**
	   * 设置所属出库批次接口
	   * @author henrysun
	   * 2018年9月3日 上午5:26:36
	   */
	  @PostMapping(value = "updateGoodsStorageOut")
	  @ResponseBody
	  public ResBody updateGoodsStorageOut(@RequestBody DeviceRoad deviceRoad) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  //查询所属出库批次是否存在
		GoodsStorageOut goodsStorageOut=goodsStorageOutService.selectById(deviceRoad.getGoodsStorageOutId());
		if(goodsStorageOut!=null){
			deviceRoadService.updateByObjCdtForErp(deviceRoad);
		}
		else{
			throw new BizApiException(ConstApiResCode.GOODS_STORAGE_OUT_NOT_EXIST);
		}
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
	  String createPrepayBack(ReqCreatePrepay reqCreatePrepay,Model model) throws SystemException {
		  logger.info("收到createPrepayBack请求：{}",reqCreatePrepay.toString());
		  String order=((Map)payService.createPrepay(reqCreatePrepay,(byte) 2).getData()).get("order").toString();
		  ReqCreateQrCode reqObj=new ReqCreateQrCode();
		  reqObj.setOrder(order);
		  reqObj.setPayType(reqCreatePrepay.getSaleType());
		  String qrcodeUrl=((Map)payService.getQrCode(reqObj).getData()).get("qrcode_url").toString();
		  String newQrCodeUrl=new String(Base64Utils.decodeFromString( URLDecoder.decode(qrcodeUrl)));
	      model.addAttribute("qrCodeUrl",URLEncoder.encode(newQrCodeUrl));
		  return "/device/road/qrcode";
		}
	  
	  /**
	   * 导出货道清单
	   * @author henrysun
	   * 2018年5月29日 下午12:27:51
	   */
	  @GetMapping(value="/roadExcel")
	  void roadExcel(@RequestParam String deviceId,HttpServletResponse response) throws SystemException  {
		  List<DeviceRoad> resultList=deviceRoadService.selectListByDeviceIdForExcel(deviceId);
		  if(resultList.size()==0){
			  throw new BizApiException(ConstApiResCode.ROADLIST_EMPTY);
		  }
		  String shortName=resultList.get(0).getShortName();
		  FileUtil.exportExcel(resultList,shortName+"-货道清单",shortName+"-货道清单",DeviceRoad.class,shortName+"-货道清单.xls",response);
		}
	  
	  /**
	   * 获取商品参考销售价
	   * @author henrysun
	   * 2018年9月3日 上午4:40:58
	   */
	  @GetMapping(value = "getRecommendPrice")
	  @ResponseBody
	  public ResBody getRecommendPrice(@RequestParam String goodsCode, Model model) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		List<Double> listPrice=deviceRoadService.selectLastSalePrice(goodsCode);
		StringBuffer sb=new StringBuffer();
		if(listPrice.size()==0){
			//再从入库批次里找到最新的三个建议零售价
			List<Double> listMarketPrice=goodsStorageInService.selectLastMarketPrice(goodsCode);
			if(listMarketPrice.size()==0){
				sb.append("无,");
			}
			else{
				for (Double item : listMarketPrice) {
					sb.append(item.toString()+"元");
					sb.append(",");
				}
			}
		}
		else{
			for (Double item : listPrice) {
				sb.append(item.toString()+"元");
				sb.append(",");
			}
		}
		resBody.setData(sb.toString().substring(0,sb.toString().length()-1));
		return resBody;
	  }
	
}
