package com.whalegoods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddleData;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.GoodsAdsMiddleMapper;
import com.whalegoods.service.GoodsAdsMiddleService;


@Service
public class GoodsAdsMiddleServiceImpl  implements GoodsAdsMiddleService {
	
	@Autowired
	GoodsAdsMiddleMapper GoodsAdsMiddleMapper;
	
	@Override
	public Map<String,Object> selectByDeviceId(Map<String, Object> condition) throws SystemException {
		List<ResGoodsAdsMiddle> list=GoodsAdsMiddleMapper.selectByDeviceId(condition);
		List<ResGoodsAdsMiddleData> list1=new ArrayList<>();
		List<ResGoodsAdsMiddleData> list2=new ArrayList<>();
		Map<String,Object> map=new HashMap<>();
		for (ResGoodsAdsMiddle item : list) {
			ResGoodsAdsMiddleData object=new ResGoodsAdsMiddleData();
			try {
				BeanUtils.copyProperties(object,item);
			} catch (Exception e) {
				throw new SystemException(ConstApiResCode.SYSTEM_ERROR);
			}
			if(item.getType()==1)
			{
				map.put("now_start_time", item.getStartTime());
				map.put("now_end_time", item.getEndTime());
				list1.add(object);
				map.put("now", list1);
			}
			if(item.getType()==2)
			{
				map.put("next_start_time", item.getStartTime());
				map.put("next_end_time", item.getEndTime());
				list2.add(object);
				map.put("next", list2);
			}
		}
		return map;
	}

}
