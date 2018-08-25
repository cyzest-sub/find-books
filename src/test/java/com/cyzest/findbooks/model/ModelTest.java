package com.cyzest.findbooks.model;

import com.cyzest.findbooks.ExceptedAssert;
import com.cyzest.findbooks.dao.User;
import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;

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

        Assertions.assertPojoMethodsForAll(pojoClasses).areWellImplemented();
    }

    @Test
    public void BookMarkSort_객채생성을_검증한다() {
        BookMarkSort bookMarkSort = BookMarkSort.REG_DATE;
        Assert.assertEquals("regDate,DESC", bookMarkSort.getCode());
        Assert.assertEquals("시간순", bookMarkSort.getDescription());
    }

    @Test
    public void DefaultAuthUser_객채생성을_검증한다() {
        ExceptedAssert.assertThrows(NullPointerException.class,
                () -> new DefaultAuthUser(null));
        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> new DefaultAuthUser(new User()));
    }

}
