package com.whalegoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.util.DateUtil;

/**
 * 货道商品信息业务逻辑实现类
 * @author henrysun
 * 2018年5月2日 下午2:33:07
 */
@Service
public class DeviceRoadServiceImpl extends BaseServiceImpl<DeviceRoad,String> implements DeviceRoadService {
	
	@Autowired
	DeviceRoadMapper deviceRoadMapper;
	
	@Autowired
	GoodsAdsMiddleService adsMiddleService;
	
	@Override
	public BaseMapper<DeviceRoad, String> getMapper() {
		return deviceRoadMapper;
	}
	
	@Override
	public List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String, Object> mapCdt) throws SystemException {
		List<ResDeviceGoodsInfo> list=deviceRoadMapper.selectByIdOfJpAndSupp(mapCdt);
		//查询该设备的促销商品
		ReqGetAdsMiddleList amObjCdt=new ReqGetAdsMiddleList();
		amObjCdt.setDevice_code_sup(mapCdt.get("deviceIdSupp").toString());
		amObjCdt.setDevice_code_wg(mapCdt.get("deviceIdJp").toString());
		List<GoodsAdsMiddle> listAdsMiddle=adsMiddleService.selectAdsMiddleList(amObjCdt);
		Date nowDate=new Date();
		for (ResDeviceGoodsInfo resDeviceGoodsInfo:list) {
			//如果adsMiddleType不为空则是促销商品
			if(resDeviceGoodsInfo.getAdsMiddleType()!=null){
				//整点
				if(resDeviceGoodsInfo.getAdsMiddleType()==1){
					for (GoodsAdsMiddle goodsAdsMiddle : listAdsMiddle) {
						//在指定的时间范围之内，则是促销商品
						if(goodsAdsMiddle.getGoodsCode().equals(resDeviceGoodsInfo.getGoodsCode())&&DateUtil.belongTime(nowDate,DateUtil.getFormatHms(goodsAdsMiddle.getStartHms(),nowDate), DateUtil.getFormatHms(goodsAdsMiddle.getEndHms(),nowDate))){
							//如果商品正在促销，则mSalePrice取代salePrice作为当前销售价
							//salePrice取代marketPrice作为原价
							Double salePrice=resDeviceGoodsInfo.getSalePrice();
							resDeviceGoodsInfo.setSalePrice(goodsAdsMiddle.getSalePrice());
							resDeviceGoodsInfo.setMarketPrice(salePrice);
							//标识为促销商品
							resDeviceGoodsInfo.setSaleType((byte) 1);
							break;
						}
						else{
							resDeviceGoodsInfo.setSaleType((byte) 2);
							continue;
						}
					}
				}
				//时间段
/*				if(resDeviceGoodsInfo.getSaleType()==2){
					
				}*/
			}
			else{
				//标识为正常价格销售商品
				resDeviceGoodsInfo.setSaleType((byte) 2);
			}
		}
		return list;
	}

	@Override
	public List<ResDeviceGoodsInfo> selectByGoodsOrPathCode(Map<String, Object> mapCdt) {
		return deviceRoadMapper.selectByGoodsOrPathCode(mapCdt);
	}

	@Override
	public int updateByObjCdtForErp(DeviceRoad objCdt) {
		return deviceRoadMapper.updateByObjCdtForErp(objCdt);
	}

	@Override
	public int deleteByDeviceId(String deviceId) {
		return deviceRoadMapper.deleteByDeviceId(deviceId);
	}

	@Override
	public int insertBatch(List<DeviceRoad> list) {
		return deviceRoadMapper.insertBatch(list);
	}

	@Override
	public List<DeviceRoad> selectListByDeviceIdForExcel(String deviceId) {
		return deviceRoadMapper.selectListByDeviceIdForExcel(deviceId);
	}

	@Override
	public int updateSalePrice(Map<String, Object> mapCdt) {
		return deviceRoadMapper.updateSalePrice(mapCdt);
	}

	@Override
	public double selectMaxPriceByGoodsCode(Map<String,Object> mapCdt) {
		return deviceRoadMapper.selectMaxPriceByGoodsCode(mapCdt);
	}

	@Override
	public List<Double> selectLastSalePrice(String goodsCode) {
		return deviceRoadMapper.selectLastSalePrice(goodsCode);
	}

}
