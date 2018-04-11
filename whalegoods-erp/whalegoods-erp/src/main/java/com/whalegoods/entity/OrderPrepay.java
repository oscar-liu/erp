package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

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
public class OrderPrepay extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String orderId;
    
    private Date orderTime;
    
    private Byte orderStatus;
    
    private String goodsCode;
    
    private String goodsName;
    
    private Double salePrice;
    
    private Byte ctn;
    
    private String pathCode;
    
    private Byte floor;
    
    private Byte payType;
    
    private String prepayId;
    
    private String deviceIdJp;
    
    private String deviceIdSupp;

}