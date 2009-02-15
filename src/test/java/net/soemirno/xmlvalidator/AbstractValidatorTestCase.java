package net.soemirno.xmlvalidator;

import static junit.framework.Assert.fail;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractValidatorTestCase {
    protected File getResourceFile(String filename) {
        return new File(this.getClass().getClassLoader().getResource(filename).getFile());
    }

    protected void assertPrivateConstructor(Class<?> aClass) {
        final Constructor<?> constructor = aClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        final Object n;
        try {
            n = constructor.newInstance((Object[]) null);
            org.junit.Assert.assertEquals("should have class instance", aClass, n.getClass());
        } catch (InstantiationException e) {
            e.printStackTrace();
            fail("exception");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("exception");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("exception");
        }
    }
}
