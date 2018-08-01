package com.cyzest.findbooks;

import org.junit.Assert;

/**
 * JUnit5 assertThrows 메서드에 대응
 */
public interface ExceptedAssert {

    static void assertThrows(Class clazz, ExceptedAssert exceptedAssert) {
        try {
            exceptedAssert.execute();
            Assert.fail();
        } catch (Throwable ex) {
            if (!clazz.isInstance(ex)) {
                Assert.fail();
            }
        }
    }

    void execute() throws Throwable;

}
