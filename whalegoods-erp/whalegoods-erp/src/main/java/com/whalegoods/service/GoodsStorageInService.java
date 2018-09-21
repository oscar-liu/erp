package com.whalegoods.service;

import java.util.List;
import java.util.Map;

import com.whalegoods.entity.GoodsStorageIn;


/**
 * 仓库入库相关业务逻辑接口
 * @author henrysun
 * 2018年8月28日 下午2:21:43
 */
public interface GoodsStorageInService extends BaseService<GoodsStorageIn,String> {
	
	List<GoodsStorageIn> getStorageInListByGoodsSkuId(Map<String,Object> mapCdt);
	
	List<Double> selectLastMarketPrice(String goodsCode);
}
