package com.whalegoods.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.entity.GoodsAdsMiddle;
import com.whalegoods.entity.request.ReqGetAdsMiddleList;
import com.whalegoods.entity.request.ReqGetAdsTopList;
import com.whalegoods.entity.response.ResBody;
import com.whalegoods.entity.response.ResGoodsAdsMiddle;
import com.whalegoods.entity.response.ResGoodsAdsMiddleOther;
import com.whalegoods.entity.response.ResGoodsAdsTop;
import com.whalegoods.exception.SystemException;
import com.whalegoods.service.GoodsAdsMiddleService;
import com.whalegoods.service.GoodsAdsTopService;
import com.whalegoods.util.DateUtil;

/**
 * 广告促销API
 * @author henry-sun
 *
 */
@RestController
@RequestMapping(value = "/v1/ads")
public class V1AdsController {
	
	  @Autowired
	  GoodsAdsTopService goodsAdsTopService;
	  
	  @Autowired
	  GoodsAdsMiddleService goodsAdsMiddleService;

	  /**
	   * 获取广告列表接口
	   * @author henrysun
	   * 2018年5月15日 上午10:44:15
	   */
	  @GetMapping(value="/getTopList")
	  ResBody getlistGoodsAdsTop(@Valid ReqGetAdsTopList objCdt) {
		  List<ResGoodsAdsTop> dataList=goodsAdsTopService.selectAdsTopList(objCdt);
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS),dataList);
		  return resBody;
		}
	  
	  /**
	   * 获取促销商品列表接口
	   * @author henrysun
	   * 2018年5月15日 上午10:44:01
	   */
	  @GetMapping(value="/getMiddleList")
	  ResBody getlistGoodsAdsMiddle(@Valid ReqGetAdsMiddleList objCdt) throws SystemException  {
		  ResBody resBody=new ResBody(ConstApiResCode.SUCCESS,ConstApiResCode.getResultMsg(ConstApiResCode.SUCCESS));
		  List<GoodsAdsMiddle> adsMiddles=goodsAdsMiddleService.selectAdsMiddleList(objCdt);
		  Date nowDate=new Date();
		  List<ResGoodsAdsMiddle> resListCurr=new ArrayList<>();
		  List<ResGoodsAdsMiddleOther> resListOther=new ArrayList<>();
		  ResGoodsAdsMiddle itemCurr=null;	
		  ResGoodsAdsMiddleOther itemOther=null;
		  outloop:for (GoodsAdsMiddle adsMiddle : adsMiddles) {
			itemOther=new ResGoodsAdsMiddleOther();
			if(adsMiddle.getType()==1){
				 if(DateUtil.belongTime(nowDate,DateUtil.getFormatHms(adsMiddle.getStartHms(),nowDate), DateUtil.getFormatHms(adsMiddle.getEndHms(),nowDate))){
					 itemCurr=new ResGoodsAdsMiddle();
					 BeanUtils.copyProperties(adsMiddle,itemCurr);
					 itemCurr.setStartTime(adsMiddle.getStartHms().substring(0,5));
					 itemCurr.setEndTime(adsMiddle.getEndHms().substring(0,5));
					 itemOther.setType((byte) 1);
					 resListCurr.add(itemCurr);
				  }
				 else{
					 itemOther.setType((byte) 2);
				 }
				 if(resListOther.size()>0){
					 for (ResGoodsAdsMiddleOther resGoodsAdsMiddleOther : resListOther) {
						if(resGoodsAdsMiddleOther.getStartTime().equals(adsMiddle.getStartHms().substring(0,5))&&resGoodsAdsMiddleOther.getEndTime().equals(adsMiddle.getEndHms().substring(0,5))){
							continue outloop;
						}
					}
					 itemOther.setStartTime(adsMiddle.getStartHms().substring(0,5));
					 itemOther.setEndTime(adsMiddle.getEndHms().substring(0,5));
					 resListOther.add(itemOther);
				 }
				 else{
					 itemOther.setStartTime(adsMiddle.getStartHms().substring(0,5));
					 itemOther.setEndTime(adsMiddle.getEndHms().substring(0,5));
					 resListOther.add(itemOther);
				 }
			}
/*			if(adsMiddle.getType()==2){
				 if(DateUtil.belongTime(nowDate,adsMiddle.getStartDate(),adsMiddle.getEndDate())){
					 item=new ResGoodsAdsMiddle();
					 BeanUtils.copyProperties(adsMiddle,item);
					 item.setStartTime(DateUtil.formatDateTime(adsMiddle.getStartDate()));
					 item.setEndTime(DateUtil.formatDateTime(adsMiddle.getEndDate()));
					 resListCurr.add(item);
				  }
				 else
				 {					 
					 if(resListOther.size()>0){
						 for (ResGoodsAdsMiddle resGoodsAdsMiddle : resListOther) {
							if(resGoodsAdsMiddle.getStartTime().equals(DateUtil.formatDateTime(adsMiddle.getStartDate()))&&resGoodsAdsMiddle.getEndTime().equals(DateUtil.formatDateTime(adsMiddle.getEndDate()))){
								continue;
							}
							 else{
								 item=new ResGoodsAdsMiddle();
								 item.setStartTime(DateUtil.formatDateTime(adsMiddle.getStartDate()));
								 item.setEndTime(DateUtil.formatDateTime(adsMiddle.getEndDate()));
								 resListOther.add(item);
							 }
						}
					 }
					 else{
						 item=new ResGoodsAdsMiddle();
						 item.setStartTime(DateUtil.formatDateTime(adsMiddle.getStartDate()));
						 item.setEndTime(DateUtil.formatDateTime(adsMiddle.getEndDate()));
						 resListOther.add(item);
					 }
				 }
			}*/
		}
		  Collections.sort(resListOther,new Comparator<ResGoodsAdsMiddleOther>() {
	            public int compare(ResGoodsAdsMiddleOther o1, ResGoodsAdsMiddleOther o2) {  
	            	return o1.getStartTime().compareTo(o2.getStartTime());
	            }  
	        });
		  Map<String,Object> mapData=new HashMap<>();
		  mapData.put("goods",resListCurr);
		  mapData.put("times",resListOther);
		  resBody.setData(mapData);
		  return resBody;
		}
}
