package com.whalegoods.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class ErpOrderList extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "订单时间", exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "2")
	private Date orderTime;
	
	@Excel(name = "点位",orderNum = "1")
	private String shortName;
	
	private String orderId;
	
	private String deviceId;
	
	@Excel(name = "订单状态",replace = {"未支付_1", "已支付_2","交易失败_3","申请退款成功_4"},orderNum = "5")
	private Byte orderStatus;
	
	@Excel(name = "商品名称",orderNum = "3")
	private String goodsName;
	
	@Excel(name = "价格",orderNum = "4")
	private Double salePrice;
	
	@Excel(name = "支付类型",replace = {"微信_1", "支付宝_2"},orderNum = "5")
	private Byte payType;
	
    private String deviceIdJp;
    
    private String startOrderTime;
    
    private String endOrderTime;
    
    private Integer prefix;
    
    private String timeRange;
    
    private Byte orderType;

	public ErpOrderList() {
	}

	public ErpOrderList(Date orderTime, String shortName, String orderId, String deviceId, Byte orderStatus,
			String goodsName, Double salePrice, Byte payType, String deviceIdJp, String startOrderTime,
			String endOrderTime, Integer prefix, String timeRange, Byte orderType) {
		super();
		this.orderTime = orderTime;
		this.shortName = shortName;
		this.orderId = orderId;
		this.deviceId = deviceId;
		this.orderStatus = orderStatus;
		this.goodsName = goodsName;
		this.salePrice = salePrice;
		this.payType = payType;
		this.deviceIdJp = deviceIdJp;
		this.startOrderTime = startOrderTime;
		this.endOrderTime = endOrderTime;
		this.prefix = prefix;
		this.timeRange = timeRange;
		this.orderType = orderType;
	}
    
}
