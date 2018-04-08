package com.whalegoods.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品分类表goods_class实体类
 * @author henry-sun
 *
 */
@Getter
@Setter
@ToString
public class GoodsClass extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String className;

}