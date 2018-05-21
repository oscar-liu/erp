package com.whalegoods.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whalegoods.constant.ConstApiResCode;
import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.ErpOrderList;
import com.whalegoods.entity.OrderList;
import com.whalegoods.exception.BizApiException;
import com.whalegoods.exception.SystemException;
import com.whalegoods.mapper.BaseMapper;
import com.whalegoods.mapper.OrderListMapper;

import com.whalegoods.service.OrderListService;
import com.whalegoods.util.DateUtil;
import com.whalegoods.util.ReType;
import com.whalegoods.util.StringUtil;

/**
 * 订单表业务操作实现类
 * @author chencong
 * 2018年4月18日 下午4:38:34
 */
@Service
public  class OrderListServiceImpl extends BaseServiceImpl<OrderList,String> implements OrderListService  {
	
	@Autowired
	private OrderListMapper orderListMapper;
	
	@Override
	public BaseMapper<OrderList, String> getMapper() {		
		return orderListMapper;
	}

	@Override
	public List<ErpOrderList> selectListByObjCdt(ErpOrderList objCdt) {
		return orderListMapper.selectListByObjCdt(objCdt);
	}

	@Override
	public ReType selectByPage(ErpOrderList objCdt, int page, int limit) throws SystemException {
		int total=this.getCountByErpObjCdt(objCdt);
		@SuppressWarnings("unused")
		Page<ErpOrderList> tPage= PageHelper.startPage(page,limit);
		List<ErpOrderList> resultList2=this.getListByObjCdt(objCdt);
		ReType reType=new ReType(total,resultList2);
		return reType;
	}
	
	@Override
	public List<ErpOrderList> getListByObjCdt(ErpOrderList orderList) throws SystemException{
			if(!StringUtil.isEmpty(orderList.getTimeRange())){
				String startTime=orderList.getTimeRange().split(ConstSysParamName.KGANG)[0];
				String endTime=orderList.getTimeRange().split(ConstSysParamName.KGANG)[1];
				
				//如果是一个月内
				if(DateUtil.countMonths(startTime,endTime,ConstSysParamName.YMD)==0){
					orderList.setStartOrderTime(startTime+ConstSysParamName.START_HMS);
					orderList.setEndOrderTime(DateUtil.getMoveDay(DateUtil.stringToDate(endTime+ConstSysParamName.START_HMS)));
					orderList.setPrefix(Integer.valueOf(orderList.getStartOrderTime().substring(0,7).replace(ConstSysParamName.GANG,"")));
					return orderListMapper.selectListByObjCdt(orderList);
				}
				//如果跨月
				else{
					if(DateUtil.countMonths(startTime,endTime,ConstSysParamName.YMD)>12){
						throw new BizApiException(ConstApiResCode.CANNOT_TWELEVE);
					}
					else{
						List<String> dateList=DateUtil.getMonthBetween(startTime,endTime,ConstSysParamName.YM);
						List<ErpOrderList> resultList=null;
						List<ErpOrderList> allResultList=new ArrayList<>();						
						for (int i = 0; i < dateList.size(); i++) {
							if(i==0){
								orderList.setStartOrderTime(startTime+ConstSysParamName.START_HMS);
							}
							else{
								orderList.setStartOrderTime(dateList.get(i)+ConstSysParamName.START_DAY+ConstSysParamName.START_HMS);
							}
							if(i==(dateList.size()-1)){
								orderList.setEndOrderTime(DateUtil.getMoveDay(DateUtil.stringToDate(endTime+ConstSysParamName.START_HMS)));
							}
							else{
								orderList.setEndOrderTime(dateList.get(i+1)+ConstSysParamName.START_DAY+ConstSysParamName.START_HMS);
							}
							orderList.setPrefix(Integer.valueOf(orderList.getStartOrderTime().substring(0,7).replace(ConstSysParamName.GANG,"")));
							resultList=orderListMapper.selectListByObjCdt(orderList);
							allResultList.addAll(resultList);
						}
					    return allResultList;
					}
				}
			}
			else
			{
				orderList.setPrefix(Integer.valueOf(DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,"")));
				return orderListMapper.selectListByObjCdt(orderList);
			}	
	  }
	
	@Override
	public int getCountByErpObjCdt(ErpOrderList orderList) throws SystemException{
			if(!StringUtil.isEmpty(orderList.getTimeRange())){
				String startTime=orderList.getTimeRange().split(ConstSysParamName.KGANG)[0];
				String endTime=orderList.getTimeRange().split(ConstSysParamName.KGANG)[1];
				
				//如果是一个月内
				if(DateUtil.countMonths(startTime,endTime,ConstSysParamName.YMD)==0){
					orderList.setStartOrderTime(startTime+ConstSysParamName.START_HMS);
					orderList.setEndOrderTime(DateUtil.getMoveDay(DateUtil.stringToDate(endTime+ConstSysParamName.START_HMS)));
					orderList.setPrefix(Integer.valueOf(orderList.getStartOrderTime().substring(0,7).replace(ConstSysParamName.GANG,"")));
					return orderListMapper.getCountByErpObjCdt(orderList);
				}
				//如果跨月
				else{
					if(DateUtil.countMonths(startTime,endTime,ConstSysParamName.YMD)>12){
						throw new BizApiException(ConstApiResCode.CANNOT_TWELEVE);
					}
					else{
						List<String> dateList=DateUtil.getMonthBetween(startTime,endTime,ConstSysParamName.YM);
						int total=0;
						List<ErpOrderList> allResultList=new ArrayList<>();						
						for (int i = 0; i < dateList.size(); i++) {
							if(i==0){
								orderList.setStartOrderTime(startTime+ConstSysParamName.START_HMS);
							}
							else{
								orderList.setStartOrderTime(dateList.get(i)+ConstSysParamName.START_DAY+ConstSysParamName.START_HMS);
							}
							if(i==(dateList.size()-1)){
								orderList.setEndOrderTime(DateUtil.getMoveDay(DateUtil.stringToDate(endTime+ConstSysParamName.START_HMS)));
							}
							else{
								orderList.setEndOrderTime(dateList.get(i+1)+ConstSysParamName.START_DAY+ConstSysParamName.START_HMS);
							}
							orderList.setPrefix(Integer.valueOf(orderList.getStartOrderTime().substring(0,7).replace(ConstSysParamName.GANG,"")));
							total+=orderListMapper.getCountByErpObjCdt(orderList);
						}
					    return total;
					}
				}
			}
			else
			{
				orderList.setPrefix(Integer.valueOf(DateUtil.getCurrentMonth().replace(ConstSysParamName.UNDERLINE,"")));
				return orderListMapper.getCountByErpObjCdt(orderList);
			}	
	  }

}
