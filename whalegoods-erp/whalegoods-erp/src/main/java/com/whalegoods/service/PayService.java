package com.whalegoods.service;


import com.whalegoods.entity.request.ReqCreatePrepay;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.entity.request.ReqRefund;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.exception.SystemException;


/**
 * 第三方支付Service接口层
 * @author henrysun
 * 2018年5月2日 上午11:41:13
 */
public interface PayService {
	
	ResBody getQrCode(ReqCreateQrCode model) throws SystemException;
	
	ResBody getOrderStatus(String orderId) throws SystemException;
	
	ResBody createPrepay(ReqCreatePrepay model) throws SystemException;
	
	ResBody refund(ReqRefund model) throws SystemException;
}
