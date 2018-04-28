package com.whalegoods.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * mapper封装公共CRUD操作
 * @author henrysun
 * 2018年4月28日 上午3:22:38
 */
public interface BaseMapper<T,E extends Serializable>{
    
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
