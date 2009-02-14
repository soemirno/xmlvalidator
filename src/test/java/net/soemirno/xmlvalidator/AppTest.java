package net.soemirno.xmlvalidator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Unit test for simple App.
 */
@RunWith(JMock.class)
public class AppTest {

    Mockery context = new JUnit4Mockery();
    private XmlValidator validator;

    @Before
    public void setUp() {
        validator = context.mock(XmlValidator.class);
        App.setValidator(validator);
    }

    @After
    public void tearDown() {
        App.setValidator(new DefaultXmlValidator());
    }

    @Test
    public void shouldCallValidatorWithArgValues() throws IOException, SAXException {
        context.checking(new Expectations() {{
            oneOf(validator).validate(with(equal(new File("schemaname"))), with(equal(new File("xmlfile"))));
        }});
        App.main(new String[]{"schemaname", "xmlfile"});
    }

    @Test
    public void testPrivateConstructors() throws
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {

        assertPrivateConstructor(App.class);
        assertPrivateConstructor(Util.class);
    }

    private void assertPrivateConstructor(Class<?> aClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        final Constructor<?> constructor = aClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        final Object n = constructor.newInstance((Object[]) null);
        assertEquals("should have class instance", aClass, n.getClass());
    }
}
