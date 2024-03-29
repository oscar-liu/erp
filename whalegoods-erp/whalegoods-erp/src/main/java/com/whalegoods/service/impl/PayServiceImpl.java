package com.whalegoods.service.impl;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstOrderStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.PayService;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.HttpUtils;
import com.whalegoods.util.IpUtil;
import com.whalegoods.util.Md5Util;
import com.whalegoods.util.NumberUtil;
import com.whalegoods.util.ShiroUtil;
import com.whalegoods.util.SmsUtil;
import com.whalegoods.util.StringUtil;
import com.whalegoods.util.XmlUtil;

/**
 * 微信和支付宝支付API调用
 * @author henrysun
 * 2018年4月15日 下午2:17:50
 */
@Service
public class PayServiceImpl implements PayService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeviceRoadService deviceRoadService;
	
	@Autowired
	private OrderListService orderListService;
	
	@Override
	public ResBody createPrepay(ReqCreatePrepay model,Byte orderType) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("deviceIdJp", model.getDevice_code_wg());
		mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
		mapCdt.put("ctn",model.getCtn());
		mapCdt.put("floor",model.getFloor());
		mapCdt.put("pathCode",model.getPathCode());
		//根据设备编号和商品编号查询商品信息
		List<ResDeviceGoodsInfo> goodsInfos=deviceRoadService.selectByGoodsOrPathCode(mapCdt);
		if(goodsInfos.size()==0){
			logger.error("货道不存在或未上架商品");
			throw new BizServiceException(ConstApiResCode.PATH_NOT_EXIST);
		}
		//判断库存状态（略过后台刷单订单）
		if(goodsInfos.get(0).getStock()==0&&orderType==1){
			logger.error("库存不足");
			throw new BizServiceException(ConstApiResCode.STOCK_NOT_ENOUGH);
		}
		ResDeviceGoodsInfo deviceGoodsInfo=null;
		Date nowDate=new Date();
		for (ResDeviceGoodsInfo resDeviceGoodsInfo : goodsInfos) {
			//如果adsMiddleType不为空则是促销商品
			if(resDeviceGoodsInfo.getAdsMiddleType()!=null){
				//整点
				if(resDeviceGoodsInfo.getAdsMiddleType()==1){
					//进入详情页的时间在指定的时间范围之内，则为促销价
					if(DateUtil.belongTime(DateUtil.timestampToDate(model.getViewTime()),DateUtil.getFormatHms(resDeviceGoodsInfo.getStartHms(),nowDate), DateUtil.getFormatHms(resDeviceGoodsInfo.getEndHms(),nowDate))){
						resDeviceGoodsInfo.setSalePrice(resDeviceGoodsInfo.getMSalePrice());
						deviceGoodsInfo=resDeviceGoodsInfo;
						break;
					}
					else{
						deviceGoodsInfo=resDeviceGoodsInfo;
						continue;
					}
				}
				//时间段
	/*				if(deviceGoodsInfo.getSaleType()==2){
					
				}*/
			}
			else{
				deviceGoodsInfo=resDeviceGoodsInfo;
			}
		}
		//生成预支付订单记录
		OrderList orderPrepay=new OrderList();
		String orderId=StringUtil.getOrderId();
		BeanUtils.copyProperties(deviceGoodsInfo,orderPrepay);
		orderPrepay.setId(StringUtil.getUUID());
		orderPrepay.setOrderId(orderId);
		orderPrepay.setOrderTime(DateUtil.getCurrentTime());
		orderPrepay.setOrderStatus((byte) 1);
		orderPrepay.setDeviceIdJp(model.getDevice_code_wg());
		orderPrepay.setDeviceIdSupp(model.getDevice_code_sup());
		orderPrepay.setPrefix(DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		orderPrepay.setOrderType(orderType);
		if(orderType==2){
			//如果是刷单订单，记录创建人
			orderPrepay.setCreateBy(ShiroUtil.getCurrentUserId());
			orderPrepay.setUpdateBy(ShiroUtil.getCurrentUserId());
		}
		orderListService.insert(orderPrepay);
		Map<String,Object> mapData=new HashMap<>();
		mapData.put("order",orderId);
		resBody.setData(mapData);
		return resBody;
	}
	
	
	@Transactional
	@Override
	public ResBody getQrCode(ReqCreateQrCode model) throws SystemException{
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Map<String,Object> mapData=new HashMap<>();
		//根据订单号查找预支付订单
		String orderId=model.getOrder();
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",orderId);
		mapCdt.put("orderStatus",ConstOrderStatus.NOT_PAY);
		mapCdt.put("prefix", DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		OrderList orderList=orderListService.selectByMapCdt(mapCdt);
		if(orderList==null){
			logger.error("预支付订单不存在，查询数据：{}",mapCdt.toString());
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		//准备参数请求第三方支付预支付API
		String goodsName=orderList.getGoodsName();
		Double salePrice=orderList.getSalePrice();
		//微信
		if(model.getPayType()==1){
			Map<String, String> mapPpRst=this.wxPrepay(goodsName, salePrice, orderId);
			if(mapPpRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX)){
				if(mapPpRst.get("result_code").equals(ConstSysParamName.SUCCESS_WX)){			
					try {
						mapData.put("qrcode_url",URLEncoder.encode(Base64Utils.encodeToUrlSafeString(mapPpRst.get("code_url").getBytes()),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						logger.error("微信预支付二维码编码失败");
						throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
					}
					resBody.setData(mapData);
					orderList.setWxPrepayId(mapPpRst.get("prepay_id"));
				}
				else {
					logger.error("微信预支付二级失败,错误代码：{},描述：{}",mapPpRst.get("err_code"),mapPpRst.get("err_code_des"));
					throw new BizServiceException(ConstApiResCode.WX_PREPAY_TWO_FAILED);
				}
			}
			else{
				logger.error("微信预支付一级失败：{}",mapPpRst.get("return_msg"));
				throw new BizServiceException(ConstApiResCode.WX_PREPAY_ONE_FAILED);
			}
		}
		//支付宝
		if(model.getPayType()==2){
			String qrCode=this.alipayPrepay(goodsName, salePrice, orderId);
			mapData.put("qrcode_url", qrCode);
			resBody.setData(mapData);
		}
		//更新预支付订单信息
		orderList.setPayType(model.getPayType());		
		try {
			orderList.setPrefix(DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
			orderListService.updateByObjCdt(orderList);
		} catch (Exception e) {
			//更新预支付记录异常不影响生成二维码
			logger.error("更新预支付记录失败：{} 原因：{}",orderList.toString(),e.getMessage());
		}
		return resBody;
	}
	
	
	@Override
	public ResBody getOrderStatus(String orderId) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Map<String,Object> mapData=new HashMap<>();
		//根据订单号查找预支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",orderId);
		mapCdt.put("prefix", DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		OrderList orderList=orderListService.selectByMapCdt(mapCdt);
		if(orderList==null){
			logger.error("预支付订单不存在，查询数据：{}",mapCdt.toString());
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		Map<String, String> mapQrRst;
		//微信
		if(orderList.getPayType()==1){
			this.wxOrderQueryDoor(orderList);
		}
		//支付宝
		else if(orderList.getPayType()==2){
			mapQrRst=this.alipayOrderQuery(orderId);
			if(mapQrRst.get("orderStatus").equals(ConstOrderStatus.PAID.toString())){
				//用于更新订单信息的新对象
				orderList.setBuyerUserId(mapQrRst.get("buyerUserId"));
				orderList.setOrderStatus(Byte.valueOf(mapQrRst.get("orderStatus")));
				orderList.setPayTime(mapQrRst.get("sendPayDate"));
			}
			else{
				this.wxOrderQueryDoor(orderList);
				if(orderList.getOrderStatus()==2){
					orderList.setPayType((byte) 1);
				}
			}

		}
		else {
			throw new BizServiceException(ConstApiResCode.PAY_TYPE_ERROR);
		}
		//更新订单信息和货道库存
		try {
			this.updateStockAndOrderInfo(orderList,null);
		} catch (Exception e) {
			//上述操作不应影响客户端
			logger.error("更新订单信息和货道库存异常："+e.getMessage());
		}
		mapData.put("order_status",orderList.getOrderStatus());
		resBody.setData(mapData);
		return resBody;
	}
	
	@Async
	@Override
	public ResBody refund(ReqRefund model) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		//根据订单号查找已支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",model.getOrder());
		mapCdt.put("orderStatus",ConstOrderStatus.PAID);
		mapCdt.put("prefix", DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		OrderList orderList=orderListService.selectByMapCdt(mapCdt);
		if(orderList==null){
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		//微信
		if(orderList.getPayType()==1){
			Map<String, String> mapRfRst = this.wxApplyRefund(orderList);	
			if(mapRfRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX))
			{
				if(!mapRfRst.get("result_code").equals(ConstSysParamName.SUCCESS_WX)){
					logger.error("微信申请退款二级失败,错误代码："+mapRfRst.get("err_code")+" 描述："+mapRfRst.get("err_code_des"));
					throw new BizServiceException(ConstApiResCode.WX_APPLY_REFUND_TWO_FAILED);
				}
			}
			else
			{
				logger.error("微信申请退款一级失败："+mapRfRst.get("return_msg"));
				throw new BizServiceException(ConstApiResCode.WX_APPLY_REFUND_ONE_FAILED);
			}
		}
		//支付宝
		else if(orderList.getPayType()==2){
			if(!this.alipayApplyRefund(orderList)){
				throw new BizServiceException(ConstApiResCode.ALIPAY_REFUND_FALID);
			}
		}
		else {
			throw new BizServiceException(ConstApiResCode.PAY_TYPE_ERROR);
		}
		try {
			//更新预支付订单信息
			orderList.setOrderStatus(ConstOrderStatus.APPLY_REFUND_SUCCESS);
			this.updateStockAndOrderInfo(orderList,model.getRefundType());
		} catch (Exception e) {
			//上述操作不应影响客户端
			logger.error("更新订单信息和货道库存异常："+e.getMessage());
		}
		return resBody;
	}
	
	@Async
	@Override
	public void refundNotify(ReqRefund model){
		//任务名称必须和后台配置的一样，否则无法获取收件人列表
		String jobName="设备退款报警";
		//短信模板
		String templateId="SMS_142015178";
		//根据订单号查询点位名称
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("orderId",model.getOrder());
		mapCdt.put("prefix", DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		ErpOrderList order=orderListService.selectDeviceByOrderId(mapCdt);
		//模板内容
		String content="{\"short_name\":\""+order.getShortName()+"\", \"goods_name\":\""+order.getGoodsName()+"\"}";
		//发送短信
		SmsUtil.sendSms(jobName,templateId,content);
	}
	

	@Override
	public ResBody closeOrder(ErpOrderList model) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		//根据订单号查找预支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",model.getOrderId());
		mapCdt.put("prefix",model.getPrefix());
		OrderList orderList=orderListService.selectByMapCdt(mapCdt);
		if(orderList==null){
			logger.error("预支付订单不存在，查询数据：{}",mapCdt.toString());
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		//微信
		if(orderList.getPayType()==1){
			Map<String, String> mapRst=this.wxCloseOrder(orderList.getOrderId());
			if(mapRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX)){
				if(mapRst.get("result_code").equals(ConstSysParamName.SUCCESS_WX)){
					return resBody;
				}
				else{
					//如果订单已支付
					if(mapRst.get("err_code").equals("ORDERPAID")){
						resBody.setResultCode(ConstApiResCode.ORDER_PAID);
						return resBody;
					}
				}
			}
			else{
				logger.error("微信关闭订单API通信失败：{}",mapRst.get("return_msg"));
				resBody.setResultCode(ConstApiResCode.SYSTEM_ERROR);
				return resBody;
			}
		}
		//支付宝
		else if(orderList.getPayType()==2){
			if(this.alipayCloseOrder(orderList.getOrderId())){
				return resBody;
			}
			else{
				resBody.setResultCode(ConstApiResCode.SYSTEM_ERROR);
				resBody.setResultMsg(null);
				return resBody;
			}
		}
		return null;
	}
	
	private OrderList wxOrderQueryDoor(OrderList orderList) throws SystemException{
		Map<String,String>	mapQrRst=this.wxOrderQuery(orderList.getOrderId());
		if(mapQrRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX))
		{
			if(mapQrRst.get("result_code").equals(ConstSysParamName.SUCCESS_WX)){
				if(mapQrRst.get("trade_state").equals(ConstSysParamName.SUCCESS_WX)){
					//用于更新订单信息的新对象
					orderList.setWxOpenId(mapQrRst.get("openid"));
					orderList.setPayTime(mapQrRst.get("time_end"));
					orderList.setOrderStatus(ConstOrderStatus.PAID);
				}
				else{
					logger.error("微信查询订单API结果：,状态："+mapQrRst.get("trade_state")+" 描述："+mapQrRst.get("trade_state_desc"));			
					if(mapQrRst.get("trade_state").equals(ConstSysParamName.WX_NOTPAY)){
						orderList.setOrderStatus(ConstOrderStatus.NOT_PAY);
					}
					else if (mapQrRst.get("trade_state").equals(ConstSysParamName.WX_REFUND)) {
						orderList.setOrderStatus(ConstOrderStatus.CLOSED);
					}
					else {
						orderList.setOrderStatus(ConstOrderStatus.PAY_FAILED);
					}
				}
			}
			else {
				logger.error("微信查询订单二级失败,错误代码："+mapQrRst.get("err_code")+" 描述："+mapQrRst.get("err_code_des"));
				orderList.setOrderStatus(ConstOrderStatus.PAY_FAILED);
			}
		}
		else
		{
			logger.error("微信查询订单一级失败："+mapQrRst.get("return_msg"));
			orderList.setOrderStatus(ConstOrderStatus.PAY_FAILED);
		}
		return orderList;
	}
	
	/**
	 * 调用微信查询订单API
	 * @author henrysun
	 * 2018年4月11日 下午3:29:30
	 * @throws SystemException 
	 */
	private  Map<String,String> wxOrderQuery(String orderId) throws SystemException{
		//定义请求数据集合
		Map<String,Object> map=new HashMap<>();
		map.put("appid",ConstSysParamValue.WX_APPID);
		map.put("mch_id",ConstSysParamValue.WX_MCHID);
		map.put("nonce_str",StringUtil.randomString(32));
		map.put("out_trade_no",orderId);
		//生成签名值
		JSONObject json=JSONObject.parseObject(JSON.toJSONString(map));
		String sign=Md5Util.getSign(json,ConstSysParamValue.WX_KEY);
		map.put("sign",sign);
		String xmlData=XmlUtil.mapToXml(map);
		logger.info("微信查询订单请求数据："+xmlData);
		String xmlResult=HttpUtils.sendPost(ConstSysParamValue.WX_QUERY,xmlData,null);
		logger.info("结果："+xmlResult);
		return XmlUtil.xmlToMap(xmlResult);
	}
	
	/**
	 * 调用支付宝查询订单API
	 * @author chencong
	 * 2018年4月11日 下午3:29:30
	 * @throws SystemException 
	 */
	private  Map<String,String> alipayOrderQuery(String orderId) throws SystemException  {
		Map<String,String> mapResult=new HashMap<>();
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				ConstSysParamValue.ALIPAY_URL,
				ConstSysParamValue.ALIPAY_APPID,
				ConstSysParamValue.ALIPAY_PRIVATE_KEY, "json","utf-8", 
				ConstSysParamValue.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeQueryRequest  request = new AlipayTradeQueryRequest();
		JSONObject sonJson=new JSONObject();
		sonJson.put("out_trade_no",orderId);
		request.setBizContent(sonJson.toJSONString());
		AlipayTradeQueryResponse  response;
		try {
			response = alipayClient.execute(request); 
		} catch (AlipayApiException e) { 
			logger.error("发送支付宝订单查询API请求失败："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
		//调用成功
		if(response.isSuccess()){
			mapResult.put("buyerUserId",response.getBuyerUserId());
			String tradeStatus=response.getTradeStatus();
			if(tradeStatus.equals(ConstSysParamName.SUCCESS_TRADE_ALIPAY)){
				mapResult.put("orderStatus",ConstOrderStatus.PAID.toString());
				mapResult.put("sendPayDate",DateUtil.formatDateTime(response.getSendPayDate()).toString());
			}
			else if (tradeStatus.equals(ConstSysParamName.ALIPAY_WAIT_BUYER_PAY)) {
				mapResult.put("orderStatus",ConstOrderStatus.NOT_PAY.toString());
			}
			else
			{
				logger.error("支付宝交易失败："+tradeStatus);
				mapResult.put("orderStatus",ConstOrderStatus.PAY_FAILED.toString());
			}
		}
		else
		{
			logger.error("调用支付宝订单查询API失败："+response.getMsg());
			mapResult.put("orderStatus",ConstOrderStatus.PAY_FAILED.toString());
		}
		return mapResult;
	}


	/**
	 * 调用微信预支付API
	 * goodsName  商品名称
	 * salePrice  商品价格
	 * orderId  商户订单号
	 * @author henrysun
	 * 2018年6月5日 下午7:19:09
	 */
	private  Map<String,String> wxPrepay(String goodsName,Double salePrice,String orderId) throws  SystemException {
		//定义请求数据集合
		Map<String,Object> map=new HashMap<>();
		map.put("appid",ConstSysParamValue.WX_APPID);
		map.put("mch_id",ConstSysParamValue.WX_MCHID);
		map.put("nonce_str",StringUtil.randomString(32));
		map.put("body",goodsName);
		map.put("out_trade_no",orderId);
		map.put("total_fee",NumberUtil.changeY2F(salePrice));
		map.put("spbill_create_ip",IpUtil.getNativeIp());
		map.put("notify_url",ConstSysParamValue.WX_NOTIFY_URL);
		map.put("trade_type","NATIVE");
		//生成签名值
		JSONObject json=JSONObject.parseObject(JSON.toJSONString(map));
		String sign=Md5Util.getSign(json,ConstSysParamValue.WX_KEY);
		map.put("sign",sign);
		String xmlData=XmlUtil.mapToXml(map);
		logger.info("wxPrepay请求数据：{}",xmlData);
		String xmlResult=HttpUtils.sendPost(ConstSysParamValue.WX_PREPAY_URL,xmlData,null);
		logger.info("结果：{}",xmlResult);
		return XmlUtil.xmlToMap(xmlResult);
	}
	
	/**
	 * 调用微信关闭订单API
	 * @author henrysun
	 * 2018年7月9日 下午3:34:22
	 */
	private  Map<String,String> wxCloseOrder(String orderId) throws  SystemException {
		//定义请求数据集合
		Map<String,Object> map=new HashMap<>();
		map.put("appid",ConstSysParamValue.WX_APPID);
		map.put("mch_id",ConstSysParamValue.WX_MCHID);
		map.put("out_trade_no",orderId);
		map.put("nonce_str",StringUtil.randomString(32));
		//生成签名值
		JSONObject json=JSONObject.parseObject(JSON.toJSONString(map));
		String sign=Md5Util.getSign(json,ConstSysParamValue.WX_KEY);
		map.put("sign",sign);
		String xmlData=XmlUtil.mapToXml(map);
		logger.info("wxCloseOrder请求数据：{}",xmlData);
		String xmlResult=HttpUtils.sendPost(ConstSysParamValue.WX_CLOSE_ORDER_URL,xmlData,null);
		logger.info("结果：{}",xmlResult);
		return XmlUtil.xmlToMap(xmlResult);
	}
	
	/**
	 * 调用支付宝预支付API
	 * @author chencong
	 * 2018年4月11日 下午3:29:30
	 * @throws SystemException 
	 */
	@SuppressWarnings("deprecation")
	private String alipayPrepay(String goodsName,Double salePrice,String orderId) throws SystemException {
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				ConstSysParamValue.ALIPAY_URL,
				ConstSysParamValue.ALIPAY_APPID,
				ConstSysParamValue.ALIPAY_PRIVATE_KEY, "json","utf-8", 
				ConstSysParamValue.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradePrecreateRequest  request = new AlipayTradePrecreateRequest();
		JSONObject sonJson=new JSONObject();
		sonJson.put("out_trade_no",orderId);
		sonJson.put("total_amount",salePrice);
		sonJson.put("subject",goodsName);
		request.setBizContent(sonJson.toJSONString());
		AlipayTradePrecreateResponse  response;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("发送支付宝预支付API请求失败："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
		//调用成功
		if(response.isSuccess()){
			return URLEncoder.encode(Base64Utils.encodeToUrlSafeString(response.getQrCode().getBytes()));
		}
		else
		{
			logger.error("调用支付宝预支付API失败："+response.getMsg());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 调用支付宝关闭订单API
	 * @author henrysun
	 * 2018年7月9日 下午3:52:25
	 */
	private boolean alipayCloseOrder(String orderId) throws SystemException {
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				ConstSysParamValue.ALIPAY_URL,
				ConstSysParamValue.ALIPAY_APPID,
				ConstSysParamValue.ALIPAY_PRIVATE_KEY, "json","GBK", 
				ConstSysParamValue.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeCancelRequest    request = new AlipayTradeCancelRequest();
		request.setBizContent("{" +
				"\"out_trade_no\":\""+orderId+"\"" +
				"}");
		AlipayTradeCancelResponse  response;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("发送支付宝关闭订单API请求失败："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
		//调用成功
		if(response.getCode().equals(ConstSysParamName.SUCCESS_ALIPAY)){
			return true;
		}
		else if(response.getSubCode().equals("ACQ.TRADE_STATUS_ERROR")){
			return false;
		}
		return false;
	}
	
	/**
	 * 调用微信申请退款API
	 * @param orderId
	 * @return
	 * @throws SystemException 
	 * @throws Exception
	 */
	private  Map<String,String> wxApplyRefund(OrderList orderList) throws SystemException {
		//定义请求数据集合
		Map<String,Object> map=new HashMap<>();
		map.put("appid",ConstSysParamValue.WX_APPID);
		map.put("mch_id",ConstSysParamValue.WX_MCHID);
		map.put("nonce_str",StringUtil.randomString(32));
		map.put("out_trade_no",orderList.getOrderId());
		map.put("out_refund_no",StringUtil.getOrderId());
		map.put("total_fee",NumberUtil.changeY2F(orderList.getSalePrice()));
		map.put("refund_fee",NumberUtil.changeY2F(orderList.getSalePrice()));
		//生成签名值
		JSONObject json=JSONObject.parseObject(JSON.toJSONString(map));
		String sign=Md5Util.getSign(json,ConstSysParamValue.WX_KEY);
		map.put("sign",sign);
		String xmlData=XmlUtil.mapToXml(map);
		logger.info("微信申请退款请求数据："+xmlData);
		//获得密钥库文件流
		InputStream is = this.getClass().getResourceAsStream("/apiclient_cert.p12");
		String xmlResult=HttpUtils.sendPostWithCert(ConstSysParamValue.WX_MCHID,is,ConstSysParamValue.WX_APPLY_REFUND,xmlData,null);
		logger.info("结果："+xmlResult);
		return XmlUtil.xmlToMap(xmlResult);
	}
	
	/**
	 * 调用支付宝申请退款API
	 * @author chencong
	 * 2018年4月11日 下午3:29:30
	 * @throws SystemException 
	 */
	private Boolean alipayApplyRefund(OrderList orderList) throws SystemException {
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				ConstSysParamValue.ALIPAY_URL,
				ConstSysParamValue.ALIPAY_APPID,
				ConstSysParamValue.ALIPAY_PRIVATE_KEY, "json","GBK", 
				ConstSysParamValue.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		JSONObject sonJson=new JSONObject();
		sonJson.put("out_trade_no",orderList.getOrderId());
		sonJson.put("refund_amount",orderList.getSalePrice());
		request.setBizContent(sonJson.toJSONString());
		AlipayTradeRefundResponse  response;
		try {
			response =  alipayClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("请求支付宝退款申请API失败："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
		//调用成功
		if(response.isSuccess()){
			return true;
		}
		else{
			logger.error("调用支付宝退款申请API结果失败："+response.getMsg());
			return false;
		}
	}
	
	/**
	 * 更新订单信息和货道库存
	 * @param orderList
	 */
	@Transactional
	private void updateStockAndOrderInfo(OrderList orderList,Byte refundType){
		orderList.setPrefix(DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,""));
		orderListService.updateByObjCdt(orderList);
		int stock=0;
		if(orderList.getOrderStatus()==ConstOrderStatus.PAID&&orderList.getOrderType()==1){
			stock=-1;
		}
		//非ERP后台退款才加库存
		if(orderList.getOrderStatus()==ConstOrderStatus.APPLY_REFUND_SUCCESS&&refundType==null){
			stock=1;
		}
		DeviceRoad deviceRoad=new DeviceRoad();
		BeanUtils.copyProperties(orderList,deviceRoad); 
		deviceRoad.setStock((Integer)stock);
		deviceRoad.setStockOrderId(orderList.getOrderId());
		//copyProperties操作后会导致salePrice被赋予值，如果是促销商品，不设置为null会导致货道价格被更新为促销价格！
		deviceRoad.setSalePrice(null);
		deviceRoadService.updateByObjCdt(deviceRoad);
	}

}
