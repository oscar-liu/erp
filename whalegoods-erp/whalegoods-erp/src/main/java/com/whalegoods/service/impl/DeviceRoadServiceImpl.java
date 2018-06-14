package com.whalegoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.DeviceRoad;
import com.whalegoods.entity.response.ResDeviceGoodsInfo;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.DeviceRoadMapper;
import com.whalegoods.service.DeviceRoadService;
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
	
	@Override
	public BaseMapper<DeviceRoad, String> getMapper() {
		return deviceRoadMapper;
	}
	
	@Override
	public List<ResDeviceGoodsInfo> selectByIdOfJpAndSupp(Map<String, Object> mapCdt) throws SystemException {
		List<ResDeviceGoodsInfo> list=deviceRoadMapper.selectByIdOfJpAndSupp(mapCdt);
		Date nowDate=new Date();
		for (ResDeviceGoodsInfo resDeviceGoodsInfo : list) {
			//如果saleType不为空则是促销商品
			if(resDeviceGoodsInfo.getSaleType()!=null){
				//整点
				if(resDeviceGoodsInfo.getSaleType()==1){
					//在指定的时间范围之内，则是促销商品
					if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(resDeviceGoodsInfo.getStartHms(),nowDate), DateUtil.getFormatHms(resDeviceGoodsInfo.getEndHms(),nowDate))){
						//如果商品正在促销，则mSalePrice取代salePrice作为当前销售价
						//salePrice取代marketPrice作为原价
						Double salePrice=resDeviceGoodsInfo.getSalePrice();
						resDeviceGoodsInfo.setSalePrice(resDeviceGoodsInfo.getMSalePrice());
						resDeviceGoodsInfo.setMarketPrice(salePrice);
						resDeviceGoodsInfo.setSaleType((byte) 1);	
					}
					//否则是正常商品
					else{
						resDeviceGoodsInfo.setSaleType((byte) 2);	
					}
				}
				//时间段
/*				if(resDeviceGoodsInfo.getSaleType()==2){
					
				}*/
			}
			else{
				resDeviceGoodsInfo.setSaleType((byte) 2);	
			}
		}
		return list;
	}

	@Override
	public ResDeviceGoodsInfo selectByGoodsOrPathCode(Map<String, Object> mapCdt) {
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

}
