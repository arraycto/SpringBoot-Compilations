package com.wjwcloud.iot.model.page;

import java.util.List;

public class PageBean<T> {
    private List<T> list;
    private int rows;
    private int totalPages;
    private int pageSize = 15;
    private int pageNo = 1;
    private int prePageNo;
    private int nextPageNo;
    private int firstPageNo = 1;
    private int lastPageNo;
    private int startNo = 1;
    private int endNo;
    private int startRowNum;
    private int endRowNum;
    private Object objectBean;
    private String sortdatafield;
    private String sortorder;
    private String url;
    private static final int FIFTEEN = 15;
    private static final int TEN = 10;
    private static final int NINE = 9;
    private static final int FIVE = 5;
    private static final int FOUR = 4;

    public PageBean() {
        this.pageSize = PageRowsUtil.getPageRows();
    }

    public Object getObjectBean() {
        return this.objectBean;
    }

    public void setObjectBean(Object objectBean) {
        this.objectBean = objectBean;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setPrePageNo(int prePageNo) {
        this.prePageNo = prePageNo;
    }

    public void setNextPageNo(int nextPageNo) {
        this.nextPageNo = nextPageNo;
    }

    public int getTotalPages() {
        this.totalPages = this.getRows() % this.pageSize == 0 ? this.rows / this.pageSize : this.rows / this.pageSize + 1;
        return this.totalPages;
    }

    public int getPrePageNo() {
        this.prePageNo = this.pageNo - 1;
        if (this.prePageNo < this.getFirstPageNo()) {
            this.prePageNo = this.getFirstPageNo();
        }

        return this.prePageNo;
    }

    public int getNextPageNo() {
        this.nextPageNo = this.pageNo + 1;
        if (this.nextPageNo > this.getLastPageNo()) {
            this.nextPageNo = this.getLastPageNo();
        }

        return this.nextPageNo;
    }

    public int getFirstPageNo() {
        return this.firstPageNo;
    }

    public void setFirstPageNo(int firstPageNo) {
        this.firstPageNo = firstPageNo;
    }

    public int getLastPageNo() {
        this.lastPageNo = this.getTotalPages();
        return this.lastPageNo;
    }

    public void setLastPageNo(int lastPageNo) {
        this.lastPageNo = lastPageNo;
    }

    public int getStart() {
        if (this.pageNo / 10 < 1) {
            return 1;
        } else if (this.pageNo == this.totalPages) {
            return this.pageNo - 9;
        } else {
            return this.getTotalPages() - this.pageNo < 10 ? this.getTotalPages() - 9 : this.pageNo - 4;
        }
    }

    public int getEnd() {
        if (this.pageNo / 10 < 1) {
            return this.getTotalPages() > 10 ? 10 : this.getTotalPages();
        } else if (this.pageNo == this.getTotalPages()) {
            return this.getTotalPages();
        } else {
            return this.getTotalPages() - this.pageNo < 10 ? this.getTotalPages() : this.pageNo + 5;
        }
    }

    public void deltip() {
        this.endNo = this.endNo + 1 - 1;
        this.startRowNum = this.startRowNum + 1 - 1;
        this.endRowNum = this.endRowNum + 1 - 1;
        this.startNo = this.startNo + 1 - 1;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getStartRowNum() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public void setStartRowNum(int startRowNum) {
        this.startRowNum = startRowNum;
    }

    public int getEndRowNum() {
        return this.pageSize;
    }

    public void setEndRowNum(int endRowNum) {
        this.endRowNum = endRowNum;
    }

    public int getStartNo() {
        return this.getStart();
    }

    public void setStartNo(int startNo) {
        this.startNo = startNo;
    }

    public int getEndNo() {
        return this.getEnd();
    }

    public void setEndNo(int endNo) {
        this.endNo = endNo;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSortdatafield() {
        return this.sortdatafield;
    }

    public void setSortdatafield(String sortdatafield) {
        this.sortdatafield = sortdatafield;
    }

    public String getSortorder() {
        return this.sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }
}
