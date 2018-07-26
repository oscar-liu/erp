package com.whalegoods.entity.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 关闭订单API请求映射实体类
 * @author henrysun
 * 2018年7月25日 下午6:43:08
 */
@Getter
@Setter
@ToString
public class ReqCloseOrder extends ReqBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="order必填")
	@Length(min=32,max=32,message="订单号长度不正确")
	private String order;
	
}
