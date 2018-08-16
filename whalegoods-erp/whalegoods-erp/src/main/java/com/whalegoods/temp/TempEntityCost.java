package com.whalegoods.temp;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TempEntityCost  implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@Excel(name = "商品编码")
    private String goodsCode;
    
	@Excel(name = "采购单价")
    private String salePrice;


}