package com.len.base;

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
	   * 根据id查询
	   * @param id
	   * @return 实体类
	   */
	  T selectByPrimaryKey(E id);
	  
	  /**
	   * 分页查询
	   * @param record
	   * @return 数据集合
	   */
	  List<T> selectListByPage(T record);
	  
	  /*public String show(T t, int page, int limit);*/
	  
	  /**
	   * 更新非空数据
	   * @param record
	   * @return
	   */
	  int updateByPrimaryKeySelective(T record);

	  /**
	   * 更新
	   * @param record
	   * @return
	   */
	  int updateByPrimaryKey(T record);
	  
	  /**
	   * 插入
	   * @param record 实体类
	   * @return
	   */
	  int insert(T record);

	  /**
	   *插入非空字段
	   * @param record
	   * @return
	   */
	  int insertSelective(T record);
	  
	  /**
	   * 根据id删除
	   * @param id
	   * @return
	   */
	  int deleteByPrimaryKey(E id);

}
