package com.whalegoods.service;


import com.whalegoods.entity.GoodsSku;


/**
 * 基础商品SKU相关业务逻辑接口
 * @author henrysun
 * 2018年5月7日 上午10:07:28
 */
public interface GoodsSkuService extends BaseService<GoodsSku,String> {
	
	/**
	 * 校验商品编号是否已存在
	 * @author henrysun
	 * 2018年5月7日 下午3:49:24
	 */
	public Boolean checkGoodsCode(String goodsCode);
}
