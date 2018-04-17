package com.whalegoods.service.impl;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstOrderStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.mapper.OrderListMapper;
import com.whalegoods.service.PayService;
import com.whalegoods.util.HttpUtils;
import com.whalegoods.util.IpUtil;
import com.whalegoods.util.Md5Util;
import com.whalegoods.util.NumberUtil;
import com.whalegoods.util.StringUtil;
import com.whalegoods.util.XmlUtil;

/**
 * 微信和支付宝支付API调用
 * @author chencong
 *
 */
@Service
public class PayServiceImpl implements PayService{
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private DeviceRoadMapper deviceRoadMapper;
	
	@Autowired
	private OrderListMapper orderListMapper;
	
	@Transactional
	@Override
	public ResBody createPrepay(ReqCreatePrepay model) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("deviceIdJp", model.getDevice_code_wg());
		mapCdt.put("deviceIdSupp",model.getDevice_code_sup());
		mapCdt.put("goodsOrPathCode",model.getPathCode());
		mapCdt.put("type",2);
		//根据设备编号和货道编号查询商品信息
		ResDeviceGoodsInfo info=deviceRoadMapper.selectByPathOrGoodsCode(mapCdt);
		if(info==null){
			throw new BizServiceException(ConstApiResCode.PATH_NOT_EXIST);
		}
		//生成预支付订单记录
		String orderId=StringUtil.getOrderId();
		OrderList orderPrepay=new OrderList();
		BeanUtils.copyProperties(info,orderPrepay);
		orderPrepay.setId(StringUtil.getUUID());
		orderPrepay.setOrderId(orderId);
		orderPrepay.setOrderTime(new Date());
		orderPrepay.setOrderStatus((byte) 1);
		orderPrepay.setDeviceIdJp(model.getDevice_code_wg());
		orderPrepay.setDeviceIdSupp(model.getDevice_code_sup());
		try {
			orderListMapper.insertOrderList(orderPrepay);
		} catch (Exception e) {
			logger.error("生成预支付记录失败，数据："+orderPrepay.toString()+" 原因："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
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
		//根据订单号查找未支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",model.getOrder());
		mapCdt.put("orderStatus",ConstOrderStatus.NOT_PAY);
		OrderList orderList=orderListMapper.selectByOrderIdAndStatus(mapCdt);
		if(orderList==null){
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		//请求第三方支付预支付API
		String goodsName=orderList.getGoodsName();
		Double salePrice=orderList.getSalePrice();
		String orderId=model.getOrder();
		//微信
		if(model.getPayType()==1){
			Map<String, String> mapPpRst;
			try {
				mapPpRst = this.wxPrepay(goodsName, salePrice, orderId);
			} catch (Exception e) {
				logger.error("执行wxPrepay()方法出错："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}	
			if(mapPpRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX))
			{
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
					logger.error("微信预支付二级失败,错误代码："+mapPpRst.get("err_code")+" 描述："+mapPpRst.get("err_code_des"));
					throw new BizServiceException(ConstApiResCode.WX_PREPAY_TWO_FAILED);
				}
			}
			else
			{
				logger.error("微信预支付一级失败："+mapPpRst.get("return_msg"));
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
			orderListMapper.updateOrderList(orderList);
		} catch (Exception e) {
			//更新预支付记录异常不影响生成二维码
			logger.error("更新预支付记录失败："+orderList.toString()+" 原因："+e.getMessage());
		}
		return resBody;
	}
	
	
	@Transactional
	@Override
	public ResBody getOrderStatus(String orderId) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		Map<String,Object> mapData=new HashMap<>();
		//根据订单号查找预支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",orderId);
		OrderList orderList=orderListMapper.selectByOrderIdAndStatus(mapCdt);
		if(orderList==null){
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		Map<String, String> mapQrRst;
		//微信
		if(orderList.getPayType()==1){
			try {
				mapQrRst = this.wxOrderQuery(orderId);
			} catch (Exception e) {
				logger.error("执行wxOrderQuery()方法出错："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}	
			if(mapQrRst.get("return_code").equals(ConstSysParamName.SUCCESS_WX))
			{
				if(mapQrRst.get("result_code").equals(ConstSysParamName.SUCCESS_WX)){
					if(mapQrRst.get("trade_state").equals(ConstSysParamName.SUCCESS_WX)){
						//用于更新订单信息的新对象
						orderList=new OrderList();
						orderList.setOrderId(orderId);
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
							orderList.setOrderStatus(ConstOrderStatus.REFUND);
						}
						else {
							orderList.setOrderStatus(ConstOrderStatus.PAY_FAILED);
						}
					}
				}
				else {
					logger.error("微信查询订单二级失败,错误代码："+mapQrRst.get("err_code")+" 描述："+mapQrRst.get("err_code_des"));
					throw new BizServiceException(ConstApiResCode.WX_QUERY_TWO_FAILED);
				}
			}
			else
			{
				logger.error("微信查询订单一级失败："+mapQrRst.get("return_msg"));
				throw new BizServiceException(ConstApiResCode.WX_QUERY_ONE_FAILED);
			}
		}
		//支付宝
		else if(orderList.getPayType()==2){
			mapQrRst=this.alipayOrderQuery(orderId);
			//用于更新订单信息的新对象
			orderList=new OrderList();
			orderList.setBuyerLogonId(mapQrRst.get("buyerLogonId").toString());
			orderList.setOrderStatus(Byte.valueOf(mapQrRst.get("orderStatus")));
			orderList.setPayTime(mapQrRst.get("sendPayDate").toString());
		}
		else {
			throw new BizServiceException(ConstApiResCode.PAY_TYPE_ERROR);
		}
		//更新订单信息
		try {
			orderListMapper.updateOrderList(orderList);
		} catch (Exception e) {
			//更新订单记录异常不影响给前端返回结果
			logger.error("更新预支付记录失败："+orderList.toString()+" 原因："+e.getMessage());
		}
		mapData.put("order_status",orderList.getOrderStatus());
		resBody.setData(mapData);
		return resBody;
	}
	
	@Transactional
	@Override
	public ResBody refund(ReqRefund model) throws SystemException {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		//根据订单号查找预支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",model.getOrder());
		OrderList orderList=orderListMapper.selectByOrderIdAndStatus(mapCdt);
		if(orderList==null){
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		Map<String, String> mapRfRst;
		//微信
		if(orderList.getPayType()==1){
			try {
				mapRfRst = this.wxApplyRefund(orderList);
			} catch (SystemException e) {
				logger.error("执行wxApplyRefund()方法出错："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}	
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
			orderList=new OrderList();
			orderList.setOrderId(model.getOrder());
			orderList.setOrderStatus(ConstOrderStatus.APPLY_REFUND_SUCCESS);
			//生成退款信息
			orderListMapper.updateOrderList(orderList);
			//生成退款记录
		} catch (Exception e) {
			//上面两个操作异常不影响给前端返回结果
			logger.error("更新预支付记录或生成退款记录失败："+orderList.toString()+" 原因："+e.getMessage());
		}
		return resBody;
	}
	
	/**
	 * 调用微信查询订单API
	 * @author chencong
	 * 2018年4月11日 下午3:29:30
	 */
	private  Map<String,String> wxOrderQuery(String orderId) throws Exception {
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
			mapResult.put("buyerLogonId",response.getParams().get("buyer_logon_id"));
			String tradeStatus=response.getParams().get("trade_status");
			if(tradeStatus.equals(ConstSysParamName.SUCCESS_TRADE_ALIPAY)){
				mapResult.put("orderStatus",ConstOrderStatus.PAID.toString());
				mapResult.put("sendPayDate",response.getParams().get("send_pay_date"));
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
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		return mapResult;
	}


	/**
	 * 调用微信预支付API
	 * @author chencong
	 * 2018年4月11日 下午3:29:30
	 */
	private  Map<String,String> wxPrepay(String goodsName,Double salePrice,String orderId) throws Exception {
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
		logger.info("微信统一下单请求数据："+xmlData);
		String xmlResult=HttpUtils.sendPost(ConstSysParamValue.WX_PREPAY_URL,xmlData,null);
		logger.info("结果："+xmlResult);
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
		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		JSONObject sonJson=new JSONObject();
		sonJson.put("out_trade_no",orderId);
		sonJson.put("total_amount",salePrice);
		sonJson.put("subject",goodsName);
		request.setBizContent(sonJson.toJSONString());
		AlipayOpenPublicTemplateMessageIndustryModifyResponse response;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("发送支付宝预支付API请求失败："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		} 
		//调用成功
		if(response.isSuccess()){
			return URLEncoder.encode(Base64Utils.encodeToUrlSafeString(response.getParams().get("qr_code").getBytes()));
		}
		else
		{
			logger.error("调用支付宝预支付API失败："+response.getMsg());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 调用微信申请退款API
	 * @param orderId
	 * @return
	 * @throws SystemException 
	 * @throws Exception
	 */
	private  Map<String,String> wxApplyRefund(OrderList orderList) throws SystemException  {
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
		else
		{
			logger.error("调用支付宝退款申请API结果失败："+response.getMsg());
			return false;
		}
	}

}
