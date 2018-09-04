package com.whalegoods.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 库位管理表goods_storage_location表实体类
 * @author henrysun
 * 2018年9月4日 下午4:27:29
 */
@Getter
@Setter
@ToString
public class GoodsStorageLocation extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String locationName;

}