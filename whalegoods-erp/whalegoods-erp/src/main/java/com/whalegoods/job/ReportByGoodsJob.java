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

import com.whalegoods.entity.ReportByGoods;
import com.whalegoods.service.ReportBaseService;
import com.whalegoods.service.ReportByGoodsService;
import com.whalegoods.util.DateUtil;

/**
 * 每天0点04分统计昨天的基本报表数据（按商品）
 * @author henrysun
 * 2018年6月25日 下午2:05:06
 */
public class ReportByGoodsJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReportBaseService reportBaseService;
	
	@Autowired
	private ReportByGoodsService reportByGoodsService;
	
    public ReportByGoodsJob() {}
  
    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) {
    	logger.info("开始执行任务：每天0点04分统计昨天的基本报表数据（按商品）");
    	Map<String,Object> mapCdt=new HashMap<>();
    	//获取昨天的日期，格式：yyyy-MM-dd
    	Calendar calendar = new GregorianCalendar();
    	String yesterday=DateUtil.getMoveDate(new Date(),calendar.DATE,-1);
    	mapCdt.put("orderDay",yesterday);
    	List<ReportByGoods> list=reportBaseService.selectListGroupByGoods(mapCdt);
    	logger.info("查询到{}条数据",list.size());
    	//批量插入到report_by_goods表中
    	if(list.size()>0){
    		reportByGoodsService.insertBatch(list);
    	}
    }

}
