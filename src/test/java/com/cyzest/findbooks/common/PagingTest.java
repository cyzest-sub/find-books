package com.cyzest.findbooks.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class PagingTest {

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_1() {

        Paging paging = Paging.builder()
                .page(1)
                .size(10)
                .totalCount(100)
                .build();

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getSize());
        Assert.assertEquals(100, paging.getTotalCount());
        Assert.assertEquals(10, paging.getTotalPage());

        Assert.assertEquals(10, paging.getPrevPageNo());
        Assert.assertEquals(1, paging.getFirstPage());
        Assert.assertEquals(10, paging.getEndPage());
        Assert.assertEquals(11, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_2() {

        Paging paging = Paging.builder()
                .page(1)
                .size(10)
                .totalCount(0)
                .build();

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(10, paging.getSize());
        Assert.assertEquals(0, paging.getTotalCount());
        Assert.assertEquals(1, paging.getTotalPage());

        Assert.assertEquals(10, paging.getPrevPageNo());
        Assert.assertEquals(1, paging.getFirstPage());
        Assert.assertEquals(1, paging.getEndPage());
        Assert.assertEquals(11, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_3() {

        Paging paging = new Paging(15, 10, 1000);

        Assert.assertEquals(15, paging.getPage());
        Assert.assertEquals(10, paging.getSize());
        Assert.assertEquals(1000, paging.getTotalCount());
        Assert.assertEquals(100, paging.getTotalPage());

        Assert.assertEquals(10, paging.getPrevPageNo());
        Assert.assertEquals(11, paging.getFirstPage());
        Assert.assertEquals(20, paging.getEndPage());
        Assert.assertEquals(21, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_4() {

        Paging paging = Paging.builder()
                .page(1)
                .size(0)
                .totalCount(100)
                .build();

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(0, paging.getSize());
        Assert.assertEquals(100, paging.getTotalCount());
        Assert.assertEquals(0, paging.getTotalPage());

        Assert.assertEquals(0, paging.getPrevPageNo());
        Assert.assertEquals(0, paging.getFirstPage());
        Assert.assertEquals(0, paging.getEndPage());
        Assert.assertEquals(0, paging.getNextPageNo());
    }

}
