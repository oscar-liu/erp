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
import com.whalegoods.common.ResBody;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.constant.ConstSysParamValue;
import com.whalegoods.entity.OrderPrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.BizServiceException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.mapper.OrderPrepayMapper;
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
	
	@Autowired
	private OrderPrepayMapper orderPrepayMapper;
	
	@Transactional
	@SuppressWarnings("deprecation")
	@Override
	public ResBody getQrCode(ReqCreateQrCode model) throws SystemException{
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
		//请求第三方支付预支付API
		String goodsName=info.getGoodsName();
		Double salePrice=info.getSalePrice();
		String orderId=StringUtil.getOrderId();
		//若是微信支付
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
					Map<String,Object> mapData=new HashMap<>();
					mapData.put("order",orderId);
					mapData.put("qrcode_url",URLEncoder.encode(Base64Utils.encodeToString(mapPpRst.get("code_url").getBytes())));
					resBody.setData(mapData);
					//记录预支付订单信息
					OrderPrepay orderPrepay=new OrderPrepay();
					BeanUtils.copyProperties(info,orderPrepay);
					orderPrepay.setId(StringUtil.getUUID());
					orderPrepay.setOrderId(orderId);
					orderPrepay.setOrderTime(new Date());
					orderPrepay.setOrderStatus((byte) 1);
					orderPrepay.setDeviceIdJp(model.getDevice_code_wg());
					orderPrepay.setDeviceIdSupp(model.getDevice_code_sup());
					orderPrepay.setPayType(model.getPayType());
					orderPrepay.setPrepayId(mapPpRst.get("prepay_id"));
					orderPrepayMapper.insert(orderPrepay);
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

}
