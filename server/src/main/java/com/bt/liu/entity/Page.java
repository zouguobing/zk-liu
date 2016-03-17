package com.bt.liu.entity;

import com.bt.liu.support.PageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binglove on 16/3/15.
 */
public class Page<T> {

    protected List<T> result;

    protected int pageSize;

    protected int pageNumber;

    protected int totalCount;


    public Page(int pageNumber, int pageSize, int totalCount) {
        this(pageNumber, pageSize, totalCount, new ArrayList<T>(0));
    }

    public Page(int pageNumber, int pageSize, int totalCount, List<T> result) {
        if (pageSize <= 0)
            throw new IllegalArgumentException(
                    "pageSize must greater than zero");
        this.pageSize = pageSize;
        this.pageNumber = PageUtils.computePageNumber(pageNumber, pageSize,
                totalCount);
        this.totalCount = totalCount;
        setResult(result);
    }

    public void setResult(List<T> elements) {
        if (elements == null)
            throw new IllegalArgumentException("result must be not null");
        this.result = elements;
    }

    /**
     * 当前页包含的数据
     *
     * @return 当前页数据源
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return pageNumber >= getLastPageNumber();
    }

    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
    public boolean isHasNextPage() {
        return getLastPageNumber() > pageNumber;
    }

    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
    public boolean isHasPreviousPage() {
        return pageNumber > 1;
    }

    /**
     * 获取最后一页页码，也就是总页数
     *
     * @return 最后一页页码
     */
    public int getLastPageNumber() {
        return PageUtils.computeLastPageNumber(totalCount, pageSize);
    }

    /**
     * 总的数据条目数量，0表示没有数据
     *
     * @return 总数量
     */
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取当前页的首条数据的行编码
     *
     * @return 当前页的首条数据的行编码
     */
    public int getThisPageFirstElementNumber() {
        return (pageNumber - 1) * getPageSize() + 1;
    }

    /**
     * 获取当前页的末条数据的行编码
     *
     * @return 当前页的末条数据的行编码
     */
    public int getThisPageLastElementNumber() {
        int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
        return getTotalCount() < fullPage ? getTotalCount() : fullPage;
    }

    /**
     * 获取下一页编码
     *
     * @return 下一页编码
     */
    public int getNextPageNumber() {
        return pageNumber + 1;
    }

    /**
     * 获取上一页编码
     *
     * @return 上一页编码
     */
    public int getPreviousPageNumber() {
        return pageNumber - 1;
    }

    /**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 当前页的页码
     *
     * @return 当前页的页码
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * 得到用于多页跳转的页码
     *
     * @return
     */
    public List<Integer> getLinkPageNumbers() {
        return PageUtils.generateLinkPageNumbers(pageNumber,
                getLastPageNumber(), 10);
    }

}
