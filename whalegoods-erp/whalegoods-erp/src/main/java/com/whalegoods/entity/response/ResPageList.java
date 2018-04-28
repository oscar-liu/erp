package com.whalegoods.entity.response;

import java.io.Serializable;
import java.util.List;

/**
 * 分页列表对象
 * @author henrysun
 * 2018年4月28日 上午3:53:01
 */
public class ResPageList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//数据总数
	public long count;

	public List<?> data;

	public ResPageList() {
	  }

	public ResPageList(long count, List<?> data) {
	    this.count = count;
	    this.data = data;
	  }
}
