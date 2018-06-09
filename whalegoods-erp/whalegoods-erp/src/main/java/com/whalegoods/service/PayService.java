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
	
	/**
	 * 生成支付二维码
	 * @author henrysun
	 * 2018年6月5日 下午7:16:30
	 */
	ResBody getQrCode(ReqCreateQrCode model) throws SystemException;
	
	ResBody getOrderStatus(String orderId) throws SystemException;
	
	/**
	 * 创建预付单
	 * orderType 订单类型 1正常 2刷单
	 * @author henrysun
	 * 2018年6月5日 下午6:11:39
	 */
	ResBody createPrepay(ReqCreatePrepay model,Byte orderType) throws SystemException;
	
	ResBody refund(ReqRefund model) throws SystemException;

	/**
	 * 退款短信通知
	 * @author henrysun
	 * 2018年6月9日 下午3:28:06
	 */
	void refundNotify(ReqRefund model);
}
