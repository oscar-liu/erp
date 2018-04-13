package com.whalegoods.service.impl;


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
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstOrderStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.OrderList;
import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
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
	@SuppressWarnings("deprecation")
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
		String orderId=StringUtil.getOrderId();
		//微信
		if(model.getPayType()==1){
			Map<String, String> mapPpRst;
			try {
				mapPpRst = this.wxPrepay(goodsName, salePrice, orderId);
			} catch (Exception e) {
				logger.error("执行wxPrepay()方法出错："+e.getMessage());
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}	
			if(mapPpRst.get("return_code").equals(ConstSysParamName.SUCCESS))
			{
				if(mapPpRst.get("result_code").equals(ConstSysParamName.SUCCESS)){					
					mapData.put("qrcode_url",URLEncoder.encode(Base64Utils.encodeToString(mapPpRst.get("code_url").getBytes())));
					resBody.setData(mapData);
					orderList.setWxPrepayId(mapPpRst.get("prepay_id"));
				}
				else {
					logger.error("微信预支付二级失败,错误代码："+mapPpRst.get("err_code")+" 描述："+mapPpRst.get("err_code_des"));
					throw new BizServiceException(ConstApiResCode.WX_PREPAY_TWO_FAILD);
				}
			}
			else
			{
				logger.error("微信预支付一级失败："+mapPpRst.get("return_msg"));
				throw new BizServiceException(ConstApiResCode.WX_PREPAY_ONE_FAILD);
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
			orderListMapper.updatePrepayidAndPayType(orderList);
		} catch (Exception e) {
			//更新预支付记录异常不影响生成二维码
			logger.error("更新预支付记录失败："+orderList.toString()+" 原因："+e.getMessage());
		}
		return resBody;
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
	private  String alipayPrepay(String goodsName,Double salePrice,String orderId) throws SystemException {
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				ConstSysParamValue.ALIPAY_PREPAY_URL,
				ConstSysParamValue.ALIPAY_APPID,
				ConstSysParamValue.ALIPAY_PRIVATE_KEY, "json","utf-8", 
				ConstSysParamValue.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		JSONObject sonJson=new JSONObject();
		sonJson.put("out_trade_no",StringUtil.getOrderId());
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
			return URLEncoder.encode(Base64Utils.encodeToString(response.getParams().get("qr_code").getBytes()));		
		}
		else
		{
			logger.error("调用支付宝预支付API返回结果失败："+response.getMsg());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
	}

	@Override
	public ResBody getOrderStatus(String orderId) {
		ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		//根据订单号查找预支付记录
		Map<String,Object> mapCdt=new HashMap<>();
		mapCdt.put("order",orderId);
		OrderList orderList=orderListMapper.selectByOrderIdAndStatus(mapCdt);
		if(orderList==null){
			throw new BizServiceException(ConstApiResCode.ORDER_PREPAY_NOT_EXIST);
		}
		/*resBody.setData(mapData);*/
		return resBody;
	}

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
			orderListMapper.insert(orderPrepay);
		} catch (Exception e) {
			logger.error("生成预支付记录失败，数据："+orderPrepay.toString()+" 原因："+e.getMessage());
			throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
		}
		Map<String,Object> mapData=new HashMap<>();
		mapData.put("order",orderId);
		return resBody;
	}

}
