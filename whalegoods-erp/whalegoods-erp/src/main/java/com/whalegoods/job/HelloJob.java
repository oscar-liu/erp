package com.whalegoods.job;

import org.quartz.JobExecutionContext;

public class HelloJob implements BaseJob{

    public HelloJob() {  

    }  

    public void execute(JobExecutionContext context) {
    	System.out.println("哈哈哈哈哈");
    }  
}
