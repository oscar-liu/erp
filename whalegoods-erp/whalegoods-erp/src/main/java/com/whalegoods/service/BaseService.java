package com.whalegoods.service;

import java.io.Serializable;
import java.util.List;

/**
 * 通用service层
 * @author chencong
 *
 * @param <T>
 * @param <E>
 */
public interface BaseService <T,E extends Serializable>{

	/**
	 * 根据主键查找一条记录
	 * @author henrysun
	 * 2018年4月28日 上午3:23:00
	 */
    T selectById(E id);
	  
    /**
     * 根据对象条件查找对象列表
     * @author henrysun
     * 2018年4月28日 上午3:23:15
     */
    List<T> selectListByCondition(T cdt);
	  
    /**
     * 分页查询
     * @author henrysun
     * 2018年4月28日 上午3:26:55
     */
	String selectByPage(T t, int page, int limit);
	  
	  /**
    /**
     * 根据对象条件更新
     * @author henrysun
     * 2018年4月28日 上午3:24:28
     */
    int updateByCondition(T cdt);
    
    /**
     * 插入新记录
     * @author henrysun
     * 2018年4月28日 上午3:24:47
     */
    int insert(T record);

    /**
     * 根据主键删除记录
     * @author henrysun
     * 2018年4月28日 上午3:25:10
     */
    int deleteById(E id);

}
