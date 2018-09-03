package com.whalegoods.service;

import java.util.List;

import com.whalegoods.entity.GoodsStorageOut;


/**
 * 仓库出库相关业务逻辑接口
 * @author henrysun
 * 2018年8月28日 下午2:22:15
 */
public interface GoodsStorageOutService extends BaseService<GoodsStorageOut,String> {
	
	List<GoodsStorageOut> selectListByObjCdtForSetDeviceRoad(GoodsStorageOut objCdt);
}
