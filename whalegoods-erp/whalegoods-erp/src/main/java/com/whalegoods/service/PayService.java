package com.whalegoods.service;


import com.whalegoods.common.ResBody;
import com.whalegoods.entity.request.ReqCreateQrCode;
import com.whalegoods.exception.SystemException;


/**
 * 第三方支付Service接口层
 * @author chencong
 *
 */
public interface PayService {
	
	ResBody getQrCode(ReqCreateQrCode model) throws SystemException;
	
}
