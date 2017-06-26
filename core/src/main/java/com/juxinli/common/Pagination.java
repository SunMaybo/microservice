package com.juxinli.common;

/**
 * Created by jacobdong on 15/7/30.
 */
public class Pagination {
    private long pageIndex;
    private long totalPage;
    private long resultSize;
    private long pageSize;

    public Pagination(long pageIndex,long pageSize, long totalPage,  long resultSize) {
        this.pageIndex = pageIndex;
        this.totalPage = totalPage;
        this.resultSize = resultSize;
        this.pageSize = pageSize;
    }

    public Pagination(){}

    public long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getResultSize() {
        return resultSize;
    }

    public void setResultSize(long resultSize) {
        this.resultSize = resultSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
