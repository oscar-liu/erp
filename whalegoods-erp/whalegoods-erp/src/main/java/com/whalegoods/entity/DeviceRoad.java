package com.whalegoods.entity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 货道信息表device_road实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class DeviceRoad extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String deviceId;
	
    private String deviceIdJp;
    
    private String deviceIdSupp;
    
    @Excel(name = "点位",orderNum = "1")
    private String shortName;
    
    private String goodsSkuId;
    
    private String goodsCode;
	
    @Excel(name = "商品名称",orderNum = "5")
	private String goodsName;
	
    @Excel(name = "柜号",orderNum = "2")
	private Byte ctn;
	
	private String stockOrderId;
	
	@Excel(name = "货道号",orderNum = "4")
	private Byte pathCode;
	
	@Excel(name = "层号",orderNum = "3")
	private Byte floor;
	
	@Excel(name = "价格",orderNum = "6")
	private Double salePrice;
	
	@Excel(name = "最大容量",orderNum = "8")
	private Byte capacity;
	
	@Excel(name = "库存",orderNum = "7")
	private Integer stock;
	
	private Byte warningNum;
	
	private Byte lackLevel;
	
	private Byte lockStatus;

	public DeviceRoad(){}
	
	public DeviceRoad(String deviceId, String deviceIdJp, String deviceIdSupp, String shortName, String goodsSkuId,
			String goodsCode, String goodsName, Byte ctn, String stockOrderId, Byte pathCode, Byte floor,
			Double salePrice, Byte capacity, Integer stock, Byte warningNum, Byte lackLevel, Byte lockStatus) {
		super();
		this.deviceId = deviceId;
		this.deviceIdJp = deviceIdJp;
		this.deviceIdSupp = deviceIdSupp;
		this.shortName = shortName;
		this.goodsSkuId = goodsSkuId;
		this.goodsCode = goodsCode;
		this.goodsName = goodsName;
		this.ctn = ctn;
		this.stockOrderId = stockOrderId;
		this.pathCode = pathCode;
		this.floor = floor;
		this.salePrice = salePrice;
		this.capacity = capacity;
		this.stock = stock;
		this.warningNum = warningNum;
		this.lackLevel = lackLevel;
		this.lockStatus = lockStatus;
	}	

}