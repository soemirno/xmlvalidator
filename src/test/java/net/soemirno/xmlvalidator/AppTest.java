package net.soemirno.xmlvalidator;

import junit.framework.Assert;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
@RunWith(JMock.class)
public class AppTest extends AbstractValidatorTestCase {

    Mockery context = new JUnit4Mockery();
    private XmlValidator validator;
    private XmlValidatorFactory validatorFactory;

    @Before
    public void setUp() {
        validator = context.mock(XmlValidator.class);
        validatorFactory = context.mock(XmlValidatorFactory.class);
    }

    @After
    public void tearDown() {
        App.setDefaultFactory();
    }

    @Test
    public void shouldCallValidatorWithArgValues() throws IOException, SAXException {
        App.setValidatorFactory(validatorFactory);
        final String[] arguments = {getResourceFile("schema.xsd").getAbsolutePath(),
                getResourceFile("valid.xml").getAbsolutePath()};

        context.checking(new Expectations() {{
            oneOf(validatorFactory).createValidator(with(equal(new File(arguments[0]))));
            will(returnValue(validator));
            oneOf(validator).validate(with(equal(new File(arguments[1]))));
        }});
        App.main(arguments);
    }

    @Test
    public void shouldLogErrors() throws IOException, SAXException {
        try {
            App.main(new String[]{"schemaname", "xmlfile"});
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            Assert.assertEquals(SAXParseException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testPrivateConstructors() {

        assertPrivateConstructor(App.class);
    }

}
