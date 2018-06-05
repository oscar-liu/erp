package com.whalegoods.config.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 此配置是为了让job实现类能够注入service
 * URL：https://www.cnblogs.com/javanoob/p/springboot_schedule.html
 * @author henrysun
 * 2018年6月4日 下午12:08:16
 */
@Component
public class SpringJobFactory extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
