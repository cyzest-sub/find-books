package com.cyzest.findbooks.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Paging {

    private static final int PAGE_RANGE = 10;

    private int page;
    private int size;
    private int totalCount;

    public Paging(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        if (size == 0) {
            return 0;
        }
        if (totalCount == 0) {
            return 1;
        }
        int totalPage = totalCount / size;
        return (totalCount % size != 0) ? totalPage + 1 : totalPage;
    }

    public int getFirstPage() {
        if (size == 0) {
            return 0;
        }
        return (page % PAGE_RANGE == 0) ? page - (PAGE_RANGE - 1) : ((page / PAGE_RANGE) * PAGE_RANGE) + 1;
    }

    public int getEndPage() {
        int firstPage = getFirstPage();
        int endPage = firstPage + PAGE_RANGE - 1;
        int totalPage = getTotalPage();
        return endPage > totalPage ? totalPage : endPage;
    }

    public int getPrevPageNo() {
        if (size == 0) {
            return 0;
        }
        int prevPageNo = page - PAGE_RANGE;
        return (prevPageNo % PAGE_RANGE == 0) ? prevPageNo : ((prevPageNo / PAGE_RANGE) * PAGE_RANGE) + PAGE_RANGE;
    }

    public int getNextPageNo() {
        if (size == 0) {
            return 0;
        }
        int nextPageNo = page + PAGE_RANGE;
        return (nextPageNo % PAGE_RANGE == 0) ? nextPageNo - (PAGE_RANGE - 1) : ((nextPageNo / PAGE_RANGE) * PAGE_RANGE) + 1;
    }

}
