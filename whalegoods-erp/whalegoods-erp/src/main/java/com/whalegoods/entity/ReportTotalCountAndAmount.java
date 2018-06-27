package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 总销量和总销售额实体类
 * @author henrysun
 * 2018年6月27日 下午3:45:35
 */
@Getter
@Setter
@ToString
public class ReportTotalCountAndAmount  extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer salesCount;
	
    private Double salesAmount;

}