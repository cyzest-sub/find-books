package com.cyzest.findbooks.dao;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;

public class EntityTest {

    @Test
    public void POJO_클래스를_검증한다() {

        final Class[] pojoClasses = {
                BookMark.class,
                BookSearchHistory.class
        }; //FIXME BookMark와 BookSearchHistory에 User가 있을때 User를 클래스 목록에 포함해서 검증하면 애러발생, 이유 확인 필요

        Assertions.assertPojoMethodsForAll(pojoClasses).areWellImplemented();

        Assertions.assertPojoMethodsFor(User.class).areWellImplemented();
    }

}
