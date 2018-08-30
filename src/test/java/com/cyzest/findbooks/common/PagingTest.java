package com.cyzest.findbooks.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class PagingTest {

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_1() {

        Paging paging = Paging.builder()
                .page(1)
                .size(10)
                .totalCount(100)
                .build();

        Assertions.assertEquals(1, paging.getPage());
        Assertions.assertEquals(10, paging.getSize());
        Assertions.assertEquals(100, paging.getTotalCount());
        Assertions.assertEquals(10, paging.getTotalPage());

        Assertions.assertEquals(10, paging.getPrevPageNo());
        Assertions.assertEquals(1, paging.getFirstPage());
        Assertions.assertEquals(10, paging.getEndPage());
        Assertions.assertEquals(11, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_2() {

        Paging paging = Paging.builder()
                .page(1)
                .size(10)
                .totalCount(0)
                .build();

        Assertions.assertEquals(1, paging.getPage());
        Assertions.assertEquals(10, paging.getSize());
        Assertions.assertEquals(0, paging.getTotalCount());
        Assertions.assertEquals(1, paging.getTotalPage());

        Assertions.assertEquals(10, paging.getPrevPageNo());
        Assertions.assertEquals(1, paging.getFirstPage());
        Assertions.assertEquals(1, paging.getEndPage());
        Assertions.assertEquals(11, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_3() {

        Paging paging = new Paging(15, 10, 1000);

        Assertions.assertEquals(15, paging.getPage());
        Assertions.assertEquals(10, paging.getSize());
        Assertions.assertEquals(1000, paging.getTotalCount());
        Assertions.assertEquals(100, paging.getTotalPage());

        Assertions.assertEquals(10, paging.getPrevPageNo());
        Assertions.assertEquals(11, paging.getFirstPage());
        Assertions.assertEquals(20, paging.getEndPage());
        Assertions.assertEquals(21, paging.getNextPageNo());
    }

    @Test
    public void 페이징모듈이_정상적으로_동작하는가_CASE_4() {

        Paging paging = Paging.builder()
                .page(1)
                .size(0)
                .totalCount(100)
                .build();

        Assertions.assertEquals(1, paging.getPage());
        Assertions.assertEquals(0, paging.getSize());
        Assertions.assertEquals(100, paging.getTotalCount());
        Assertions.assertEquals(0, paging.getTotalPage());

        Assertions.assertEquals(0, paging.getPrevPageNo());
        Assertions.assertEquals(0, paging.getFirstPage());
        Assertions.assertEquals(0, paging.getEndPage());
        Assertions.assertEquals(0, paging.getNextPageNo());
    }

}
