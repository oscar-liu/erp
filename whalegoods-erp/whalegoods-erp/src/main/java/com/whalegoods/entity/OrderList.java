package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 预支付表order_prepay实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class OrderList extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String deviceIdJp;
    
    private String deviceIdSupp;

    private String goodsCode;
	
    private String orderId;
    
    private String orderTime;
    
    private Byte orderStatus;
    
    private String goodsName;
    
    private Double salePrice;
    
    private Byte ctn;
    
    private Byte pathCode;
    
    private Byte floor;
    
    private Byte payType;
    
    private String wxPrepayId;
    
    private String wxOpenId;
    
    private String buyerUserId;
    
    private String payTime;
    
    private String prefix;
    
    private Byte orderType;
    
    private Double costPrice;

}