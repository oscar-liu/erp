package com.whalegoods.config.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * log注解
 * @author henry-sun
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Log {
	
    public enum LOG_TYPE{ADD,UPDATE,DEL,SELECT,ATHOR};
    
    /**内容*/
    String desc();
    
    /**类型 curd*/
    LOG_TYPE type() default LOG_TYPE.ATHOR;
}
