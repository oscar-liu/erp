package com.whalegoods.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * mapper封装公共CRUD操作
 * @author chencong
 *
 * @param <T>
 * @param <E>
 */
public interface BaseMapper<T,E extends Serializable>{

    T selectByPrimaryKey(E id);
    
    T selectById(E id);
    
    List<T> selectListByPage(T record);
    
    int updateByPrimaryKey(T record);
    
    int updateByPrimaryKeySelective(T record);

    int insert(T record);

    int insertSelective(T record);

    int deleteByPrimaryKey(E id);
   
}
