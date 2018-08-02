package com.whalegoods.temp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;

public class UserExcelHandler  extends ExcelDataHandlerDefaultImpl<TempEntityCost> {
	
	private static Logger logger = LoggerFactory.getLogger(UserExcelHandler.class);
	
	@Override
	public Object importHandler(TempEntityCost obj, String name, Object value) {
		logger.info(name+":"+value);
		return super.importHandler(obj, name, value);
	}
}
