package com.whalegoods.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * public interface BaseJob extends StatefulJob,Serializable
 * 采用StatefulJob,使jobDetails顺序执行,只有前一次job运行完成后，才会运行本次job;
 而多个jobDetails可以并行,互不影响，因为它们是多个线程各自运行;
 缺点：
 trigger 有阻塞情况,解决方法:
 1.将trigger的单次触发时间调节合理；
 2.优化job执行代码,节省运行时间
 以上StatefulJob类，经过实际验证。
 */
public interface BaseJob extends Job{
	
	public void execute(JobExecutionContext context) throws JobExecutionException;
}
