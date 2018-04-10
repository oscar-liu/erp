package com.whalegoods.service;

import org.springframework.stereotype.Service;

/**
 * 第三方支付Service接口层
 * @author chencong
 *
 */
@Service
public class PayService {
	
	//统一下单API地址
	private static String wx_prepay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";
	//微信公众账号ID
	private static String wx_app_id="";
	//微信商户号
	private static String wx_mch_id="";
}
