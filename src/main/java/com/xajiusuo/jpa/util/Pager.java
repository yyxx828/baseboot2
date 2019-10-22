package com.xajiusuo.jpa.util;

import java.util.ArrayList;
import java.util.List;


/**
 * Bean类 - 分页
 */

public class Pager {

	// 排序方式
	public enum OrderType {
		asc, desc
	}

	public static final Integer MAX_PAGE_SIZE = 50000; // 每页最大记录数限制

	private Integer pageNumber = 1; // 当前页码
	private Integer pageSize = 200; // 每页记录数
	private Integer totalCount = 0; // 总记录数
	private Integer pageCount = 0; // 总页数
	private String property; // 查找属性名称
	private String keyword; // 查找关键字
	private Integer uuid;
	private String typeId;
	private String typeProperty;
	private String orderField; // 排序字段
	private OrderType orderDirection = OrderType.desc; // 排序方式
	private List list; // 数据List
	private Integer[] lastDocs;


	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeProperty() {
		return typeProperty;
	}

	public void setTypeProperty(String typeProperty) {
		this.typeProperty = typeProperty;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public OrderType getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(OrderType orderDirection) {
		this.orderDirection = orderDirection;
	}

	public Integer getUuid() {
		return uuid;
	}

	public void setUuid(Integer uuid) {
		this.uuid = uuid;
	}

	public Integer[] getLastDocs() {
		return lastDocs;
	}

	public void setLastDocs(Integer[] lastDocs) {
		this.lastDocs = lastDocs;
	}





	/**
	 * LastDocs转String，页面存储
	 *
	 * @return
	 */
	public String getLastDocsText() {
		if(this.lastDocs == null){
			return "";
		}
		List<String> doccount = new ArrayList();
		for (Integer ii : this.lastDocs) {
			doccount.add(ii + "");
		}
		return doccount.toString();
	}







}