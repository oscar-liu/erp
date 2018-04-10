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
    
    private String prcodeUrl;
    
    private Byte orderStatus;
    
    private String goodsSkuId;
    
    private String goodsName;
    
    private Double salePrice;
    
    private String spec;
    
    private Double oneCost;
    
    private String deviceRoadId;
    
    private String deviceIdJp;
    
    private Date deviceIdSupp;
    
    private Byte ctn;
    
    private String pathCode;
    
    private Byte floor;
    
    private Byte payType;
    
    private String prepayId;
    
    private String prepayStatusCode;
    
    private String prepayInfo;
    
    private String prepayResult;
    
    private String prepayErrorCode;
    
    private String prepayErrorInfo;

}