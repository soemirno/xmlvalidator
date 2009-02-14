package net.soemirno.xmlvalidator;

import static junit.framework.Assert.assertEquals;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
@RunWith(JMock.class)
public class AppTest {

    Mockery context = new JUnit4Mockery();
    private XmlValidator validator;
    private Logger logger;

    @Before
    public void setUp() {
        validator = context.mock(XmlValidator.class);
        logger = context.mock(Logger.class);
        App.setLogger(logger);
    }

    @After
    public void tearDown() {
        App.setValidator(new App());
    }

    @Test
    public void shouldCallValidatorWithArgValues() {
        App.setValidator(validator);
        context.checking(new Expectations() {{
            oneOf(validator).validate(with(equal(new File("schemaname"))), with(equal(new File("xmlfile"))));
        }});

        App.main(new String[]{"schemaname", "xmlfile"});
    }

    @Test
    public void shouldLogErrors() {
        context.checking(new Expectations() {{
            oneOf(logger).error(with(any(String.class)), with(any(Exception.class)));
        }});
        App.main(new String[]{"schemaname", "xmlfile"});
    }

    @Test
    public void shouldLogValidationStartAndEnd() {
        context.checking(new Expectations() {{
            oneOf(logger).info(with(equal("validating valid.xml with schema.xsd")));
            oneOf(logger).info(with(equal("finished validating valid.xml with schema.xsd")));
        }});
        XmlValidator validator = new App();
        validator.validate(getResourceFile("schema.xsd"), getResourceFile("valid.xml"));
    }

    private File getResourceFile(String filename) {
        return new File(this.getClass().getClassLoader().getResource(filename).getFile());
    }

    @Test
    public void shouldReturnSchemaNotFoundError() {
        XmlValidator validator = new App();
        try {
            validator.validate(new File("schemaname"), getResourceFile("valid.xml"));
            fail("should throw exception");
        } catch (App.XmlValidatorException e) {
            assertEquals("should catch IOException", e.getCause().getClass(), IOException.class);
        }

    }

    @Test
    public void shouldCatchFatalException() {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
        }});
        XmlValidator validator = new App();
        try {
            validator.validate(getResourceFile("invalid_schema.xsd"), getResourceFile("valid.xml"));
            fail("should throw exception");
        } catch (App.XmlValidatorException e) {
            assertEquals("should catch Sax exception", e.getCause().getClass(), SAXParseException.class);
        }
    }

    @Test
    public void shouldReturnSourceNotFoundError() {
        XmlValidator validator = new App();
        try {
            validator.validate(getResourceFile("schema.xsd"), new File("dummy.xml"));
            fail("should throw exception");
        } catch (App.XmlValidatorException e) {
            assertEquals("should catch IOException", e.getCause().getClass(), IOException.class);
        }

    }

    @Test
    public void shouldFormatMessageWithLocationInfo() {
        assertEquals("Line 1, column 1 : errormessage",
                App.createMessage(new SAXParseException("errormessage", "", "", 1, 1)));
    }

    @Test
    public void shouldLogValidationError() throws SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).error(with(equal("Line 1, column 1 : errormessage")));
        }});

        App.ValidationErrorLogger logger = new App.ValidationErrorLogger();
        logger.error(new SAXParseException("errormessage", "", "", 1, 1));
    }

    @Test
    public void shouldLogValidationWarning() throws SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).warn(with(equal("Line 1, column 1 : warning")));
        }});

        App.ValidationErrorLogger logger = new App.ValidationErrorLogger();
        logger.warning(new SAXParseException("warning", "", "", 1, 1));
    }

    @Test
    public void shouldReThrowFatalError() throws SAXException {
        try {
            App.ValidationErrorLogger logger = new App.ValidationErrorLogger();
            logger.fatalError(new SAXParseException("warning", "", "", 1, 1));
            fail("should throw exception");
        } catch (App.XmlValidatorException e) {
            assertEquals("should catch SAXParseException", e.getCause().getClass(), SAXParseException.class);
        }
    }

}
