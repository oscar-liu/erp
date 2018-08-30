package com.whalegoods.mapper;

import java.util.List;

import com.whalegoods.entity.GoodsStorageIn;


public interface GoodsStorageInMapper extends BaseMapper<GoodsStorageIn, String>{
	
	/**
	 * 根据商品编号查询对应的仓库入库批次
	 * @author henrysun
	 * 2018年8月30日 下午5:21:48
	 */
	List<GoodsStorageIn> getStorageInListByGoodsSkuId(String goodsSkuId);
}