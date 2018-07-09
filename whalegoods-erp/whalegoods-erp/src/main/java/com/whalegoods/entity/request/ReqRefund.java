package com.whalegoods.entity.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款API 请求映射实体类 
 * @author henrysun
 * 2018年5月9日 上午10:27:42
 */
@Getter
@Setter
@ToString
public class ReqRefund implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="order必填")
	@Length(min=32,max=32,message="订单号长度不正确")
	private String order;
	
	//2表示ERP后台退款
	private Byte refundType;
	
}
