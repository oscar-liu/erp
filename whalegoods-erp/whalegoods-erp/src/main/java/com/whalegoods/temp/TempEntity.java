package com.whalegoods.temp;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TempEntity  implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@Excel(name = "售货机编码")
    private String deviceIdSupp;
    
	@Excel(name = "交易编号")
    private String orderId;
    
	@Excel(name = "商品编码")
    private String goodsCode;
    
	@Excel(name = "商品名称")
    private String goodsName;
    
	@Excel(name = "商品价格")
    private String salePrice;
    
	@Excel(name = "支付方式")
    private String payWay;
    
	@Excel(name = "退款状态")
    private String orderStatus;
    
	@Excel(name = "交易时间", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

}