package com.whalegoods.core.listener;

import com.whalegoods.core.quartz.JobTask;
import com.whalegoods.entity.SysJob;
import com.whalegoods.service.JobService;
import com.whalegoods.service.RoleService;
import com.whalegoods.util.SpringUtil;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 启动数据库中已经设定为 启动状态(status:true)的任务 项目启动时init
 * @author henry-sun
 *
 */
@Configuration
public class DataSourceJobThread extends Thread {

  private static final Logger log = LoggerFactory.getLogger(DataSourceJobThread.class);
  
  @Autowired
  RoleService roleService;

  @Autowired
  JobService jobService;

  @Override
  public void run() {
    try {
      Thread.sleep(1000);
      log.info("---------线程启动---------");
      JobTask jobTask = SpringUtil.getBean("jobTask");
      SysJob job = new SysJob();
      job.setStatus(true);
      List<SysJob> jobList = jobService.selectListByCondition(job);
      //开启任务
      jobList.forEach(jobs -> {
        log.info("---任务["+jobs.getId()+"]系统 init--开始启动---------");
        jobTask.startJob(jobs);
          }
      );
      if(jobList.size()==0){
        log.info("---数据库暂无启动的任务---------");
      }else
      System.out.println("---任务启动完毕---------");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
