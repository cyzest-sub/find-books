package com.cyzest.findbooks.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Paging {

    private final int pageRange = 10;

    private int page;
    private int size;
    private int totalCount;

    public Paging(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        if (size == 0 || pageRange == 0) {
            return 0;
        }
        if (totalCount == 0) {
            return 1;
        }
        int totalPage = totalCount / size;
        return (totalCount % size != 0) ? totalPage + 1 : totalPage;
    }

    public int getFirstPage() {
        if (size == 0 || pageRange == 0) {
            return 0;
        }
        return (page % pageRange == 0) ? page - (pageRange - 1) : ((page / pageRange) * pageRange) + 1;
    }

    public int getEndPage() {
        int firstPage = getFirstPage();
        int endPage = firstPage + pageRange - 1;
        int totalPage = getTotalPage();
        return endPage > totalPage ? totalPage : endPage;
    }

    public int getPrevPageNo() {
        if (size == 0 || pageRange == 0) {
            return 0;
        }
        int prevPageNo = page - pageRange;
        return (prevPageNo % pageRange == 0) ? prevPageNo : ((prevPageNo / pageRange) * pageRange) + pageRange;
    }

    public int getNextPageNo() {
        if (size == 0 || pageRange == 0) {
            return 0;
        }
        int nextPageNo = page + pageRange;
        return (nextPageNo % pageRange == 0) ? nextPageNo - (pageRange - 1) : ((nextPageNo / pageRange) * pageRange) + 1;
    }

}
