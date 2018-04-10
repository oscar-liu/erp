package com.whalegoods.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiStatus;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.PayService;
import com.whalegoods.util.HttpUtils;
import com.whalegoods.util.IpUtil;
import com.whalegoods.util.Md5Util;
import com.whalegoods.util.NumberUtil;
import com.whalegoods.util.StringUtil;
import com.whalegoods.util.XmlUtil;


@Service
public class PayServiceImpl implements PayService{
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private DeviceRoadMapper deviceRoadMapper;
	
	@Override
	public ResBody getQrCode(ReqCreateQrCode model) throws Exception{
		Map<String,Object> condition=new HashMap<>();
		condition.put("deviceIdJp", model.getDeviceCodeWg());
		condition.put("deviceIdSupp",model.getDeviceCodeSupp());
		condition.put("type",2);
		ResDeviceGoodsInfo info=deviceRoadMapper.selectByPathOrGoodsCode(condition);
		if(info==null)
		{
			ResBody resBody=new ResBody(ConstApiStatus.SUCCESS, resultMsg)
		}
		
		String goodsName=info.getGoodsName();
		Double salePrice=info.getSalePrice();
		String orderId=StringUtil.getOrderId();
		Map<String, String> reqResult=this.wxPrepay(goodsName, salePrice, orderId);
		if(reqResult.get("return_code").equals(ConstSysParamName.SUCCESS))
		{
			
		}
		else
		{
			
		}
		return null;
	}
	
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
		String xmlData=XmlUtil.outputXml(map,Map.class);
		logger.info("微信统一下单请求数据："+xmlData);
		String xmlResult=HttpUtils.sendPost(ConstSysParamValue.WX_PREPAY_URL,xmlData,null);
		logger.info("结果："+xmlResult);
		return XmlUtil.parseXml(xmlResult);
	}

}
