package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 后天系统查询订单列表  返回结果data字段  实体类
 * @author chencong
 * 2018年4月9日 下午3:04:12
 */
@Getter
@Setter
@ToString
public class ErpOrderList implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;
	
	private String shortName;
	
	private String orderId;
	
	private Byte orderStatus;
	
	private String goodsName;
	
	private Double salePrice;
	
	private Byte payType;
	
    private String deviceIdJp;
    
    private String startOrderTime;
    
    private String endOrderTime;
    
    private String prefix;
	
}
