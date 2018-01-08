package org.bham.btcert.model;

import java.util.List;

/**
 * 
* @Title: PageModel.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class PageModel<T> {

	//查詢條件
	private String queryObject;
	//排序字段
	private String sortColumn;
	//排序方式
	private String sortType; //ASC, DESC;
	// 结果集
	private List<T> rows;
	// 总数
	private int total;
	// 每页多少条数据
	private int pageSize = 20;
	// 第几页
	private int pageNumber = 1;
	// 跳过几条数
	private int skip = 0;

	public String getQueryObject() {
		return queryObject;
	}

	public void setQueryObject(String queryObject) {
		this.queryObject = queryObject;
	}
	
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
 
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

}
