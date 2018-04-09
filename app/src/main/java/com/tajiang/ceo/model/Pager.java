/*
 * Copyright (c) 2016
 * 杭州塔酱科技有限公司 版权所有
 * Created by ${USER} on ${DATE}.
 */

package com.tajiang.ceo.model;

import java.util.List;

public class Pager<T> {

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	private int page;
	private int pageSize;
	private int totalCount;
	private int totalPageCount;

		private List<T> list;

		/**
		 * 分页处理
		 * @param currentPage 当前页
		 * @param pageSize 每页的数量
		 * @param totalCount 总记录数
		 */
		public void paging(int currentPage, int pageSize, int totalCount){
			this.page = currentPage;
			this.pageSize = pageSize;
			this.totalCount = totalCount;
			
			if(currentPage < 1){
				this.page = 1;
			}
			
//			this.totalPageCount = (this.totalCount + pageSize - 1)/pageSize;
//			this.firstPage =1;
//			this.lastPage = totalPage;
//
//			if(this.currentPage > 1){
//				this.prePage = this.currentPage - 1;
//			}else{
//				this.prePage = 1;
//			}
//
//			if(this.currentPage < totalPage){
//				this.nextPage = this.currentPage + 1;
//			}else{
//				this.nextPage = totalPage;
//			}
//
//			if(this.currentPage <= 1){
//				this.isFirst = true;
//			}else{
//				this.isFirst = false;
//			}
//
//			if(this.currentPage >= totalPage){
//				this.isLast = true;
//			}else{
//				this.isLast = false;
//			}
		}

}
