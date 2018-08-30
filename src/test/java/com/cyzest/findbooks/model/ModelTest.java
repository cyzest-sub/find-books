package com.cyzest.findbooks.model;

import com.cyzest.findbooks.dao.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelTest {

    @Test
    public void POJO_클래스를_검증한다() {

        final Class[] pojoClasses = {
                BookMarkInfo.class,
                BookMarkPagingParam.class,
                BookMarkResult.class,
                BookSearchHistoryInfo.class,
                BookSearchHistoryResult.class,
                OpenApiBookSearchParam.class
        };

        pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsForAll(pojoClasses).areWellImplemented();
    }

    @Test
    public void BookMarkSort_객채생성을_검증한다() {
        BookMarkSort bookMarkSort = BookMarkSort.REG_DATE;
        Assertions.assertEquals("regDate,DESC", bookMarkSort.getCode());
        Assertions.assertEquals("시간순", bookMarkSort.getDescription());
    }

    @Test
    public void DefaultAuthUser_객채생성을_검증한다() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new DefaultAuthUser(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new DefaultAuthUser(new User()));
    }

}
