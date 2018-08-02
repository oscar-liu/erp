package com.whalegoods.temp;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whalegoods.exception.SystemException;
import com.whalegoods.job.BaseJob;
import com.whalegoods.util.FileUtil;
/**
 * 临时转移订单任务
 * @author henrysun
 * 2018年8月2日 下午3:12:51
 */
public class TempJob implements BaseJob{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public TempJob() {}

	public void execute(JobExecutionContext context) {
	      String filePath = "C:\\Users\\guidl\\Desktop\\商品销售列表2018-08-02-14-58-28.xlsx";
	      try {
			List<TempEntity> personList = FileUtil.importExcel(filePath,1,0,TempEntity.class);
			logger.info("导入数据一共{}行",personList.size());
		} catch (SystemException e) {
			logger.error("任务执行出错：{}",e.getMessage());
		}
    }

}
