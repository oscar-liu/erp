package com.whalegoods.mapper;

import com.whalegoods.entity.GoodsSku;


public interface GoodsSkuMapper extends BaseMapper<GoodsSku, String>{
	

	/**
	 * 根据商品编号查询商品SKU数量
	 * @author henrysun
	 * 2018年5月7日 下午3:50:45
	 */
	Integer checkGoodsCode(String goodsCode);
}