package com.whalegoods.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whalegoods.constant.ConstSysParamName;
import com.whalegoods.entity.ReportBase;
import com.whalegoods.service.OrderListService;
import com.whalegoods.service.ReportBaseService;
import com.whalegoods.util.DateUtil;

/**
 * 每天0点01分统计昨天的销售数据，按设备按商品
 * @author henrysun
 * 2018年6月21日 上午11:23:59
 */
public class ReportBaseJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrderListService orderListService;
	
	@Autowired
	private ReportBaseService reportBaseService;
	
    public ReportBaseJob() {}
  
    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) {
    	logger.info("开始执行任务：每天0点01分统计昨天的销售数据，按设备按商品分组");
    	Map<String,Object> mapCdt=new HashMap<>();
    	//获取昨天的日期，格式：yyyy-MM-dd
    	Calendar calendar = new GregorianCalendar();
    	String yesterday=DateUtil.getMoveDate(new Date(),calendar.DATE,-1);
    	mapCdt.put("prefix",Integer.valueOf(yesterday.replace(ConstSysParamName.GANG,"").substring(0,6)));
    	mapCdt.put("startOrderTime",yesterday+ConstSysParamName.START_HMS);
    	mapCdt.put("endOrderTime",yesterday+ConstSysParamName.END_HMS);
    	List<ReportBase> list=orderListService.selectReportBaseList(mapCdt);
    	logger.info("查询到{}条数据",list.size());
    	//批量插入到reportBases表中
    	if(list.size()>0){
    		reportBaseService.insertBatch(list);	
    	}
    }

}
