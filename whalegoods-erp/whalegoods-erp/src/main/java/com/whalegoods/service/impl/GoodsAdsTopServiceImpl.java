package com.whalegoods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalegoods.entity.GoodsAdsTop;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.GoodsAdsTopMapper;
import com.whalegoods.service.GoodsAdsTopService;

@Service
public class GoodsAdsTopServiceImpl extends BaseServiceImpl<GoodsAdsTop,String> implements GoodsAdsTopService {
	
	@Autowired
	GoodsAdsTopMapper GoodsAdsTopMapper;
	
	@Override
	public BaseMapper<GoodsAdsTop,String> getMapper() {
		return GoodsAdsTopMapper;
	}

	@Override
	public List<Map<String,Object>> selectAdsTopList(Map<String, Object> mapCdt) {
		List<Map<String,Object>> listMapData=new ArrayList<>();
		List<ResGoodsAdsTop> list=GoodsAdsTopMapper.selectAdsTopList(mapCdt);
		if(list!=null&&list.size()>0)
		{
			for (ResGoodsAdsTop resGoodsAdsTop : list) {
				Map<String,Object> mapData=new HashMap<>();	
				Map<String,Object> mapDataSon=new HashMap<>();
				mapDataSon.put("goods_code", resGoodsAdsTop.getGoodsCode());
				mapData.put("action_data",mapDataSon);
				mapData.put("action_type", resGoodsAdsTop.getActionType());
				mapData.put("big_pic_url", resGoodsAdsTop.getBigPicUrl());
				mapData.put("tiny_pic_url", resGoodsAdsTop.getTinyPicUrl());
				listMapData.add(mapData);
			}			
		}

		return listMapData;
	}

}
