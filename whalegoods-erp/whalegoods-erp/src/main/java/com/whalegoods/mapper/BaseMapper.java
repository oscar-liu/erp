package com.whalegoods.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
     * 根据对象条件查找一条记录
     * @author henrysun
     * 2018年4月29日 下午10:10:35
     */
    T selectByObjCdt(T objCdt);
    
    /**
     * 根据Map条件查找一条记录
     * @author henrysun
     * 2018年4月29日 下午10:11:16
     */
    T selectByMapCdt(Map<String,Object> mapCdt);
    
    /**
     * 根据对象条件查找记录列表
     * @author henrysun
     * 2018年4月28日 上午3:23:15
     */
    List<T> selectListByObjCdt(T objCdt);
    
    /**
     * 根据Map条件查找记录列表
     * @author henrysun
     * 2018年4月29日 下午10:12:51
     */
    List<T> selectListByMapCdt(Map<String,Object> mapCdt);
    
    
    /**
     * 根据对象条件更新
     * @author henrysun
     * 2018年4月28日 上午3:24:28
     */
    int updateByObjCdt(T objCdt);
    
    /**
     * 根据Map条件更新
     * @author henrysun
     * 2018年4月28日 上午3:24:28
     */
    int updateByMapCdt(Map<String,Object> mapCdt);
    
    /**
     * 插入新记录
     * @author henrysun
     * 2018年4月28日 上午3:24:47
     */
    int insert(T obj);

    /**
     * 根据主键删除记录
     * @author henrysun
     * 2018年4月28日 上午3:25:10
     */
    int deleteById(E id);
    
    /**
     * 查询记录总数
     * @author henrysun
     * 2018年4月29日 下午10:47:52
     */
    int selectCount();
    
    /**
     * 根据Map条件查询记录总数
     * @author henrysun
     * 2018年4月29日 下午10:57:54
     */
    int selectCountByMapCdt(Map<String,Object> mapCdt);
    
    /**
     * 根据对象条件查询记录总数
     * @author henrysun
     * 2018年4月29日 下午10:57:54
     */
    int selectCountByObjCdt(T objCdt);
   
}
