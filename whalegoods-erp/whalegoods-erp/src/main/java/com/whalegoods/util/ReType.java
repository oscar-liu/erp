package com.whalegoods.util;

import java.io.Serializable;
import java.util.List;

public class ReType  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**状态*/
	  public int code=0;
	  /**状态信息*/
	  public String msg="";
	  /**数据总数*/
	  public long count;

	  public List<?> data;

	  public ReType() {
	  }

	  public ReType(long count, List<?> data) {
	    this.count = count;
	    this.data = data;
	  }
}
