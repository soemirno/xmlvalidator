package net.soemirno.xmlvalidator;

import static junit.framework.Assert.assertEquals;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;

@RunWith(JMock.class)
public class DefaultXmlValidatorTest {

    Mockery context = new JUnit4Mockery();

    private Logger logger;

    @Before
    public void setUp() {
        logger = context.mock(Logger.class);
    }

    @Test
    public void shouldLogErrors() throws IOException, SAXException {
        try {
            App.main(new String[]{"schemaname", "xmlfile"});
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals(e.getCause().getClass(), IOException.class);
        }
    }

    @Test
    public void shouldLogValidationStartAndEnd() throws IOException, SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).info(with(equal("validating valid.xml with schema.xsd")));
            oneOf(logger).info(with(equal("finished validating valid.xml with schema.xsd")));
        }});
        XmlValidator validator = new DefaultXmlValidator(logger);
        validator.validate(getResourceFile("schema.xsd"), getResourceFile("valid.xml"));
    }

    private File getResourceFile(String filename) {
        return new File(this.getClass().getClassLoader().getResource(filename).getFile());
    }

    @Test
    public void shouldReturnSchemaNotFoundError() throws IOException, SAXException {
        XmlValidator validator = new DefaultXmlValidator(logger);
        try {
            validator.validate(new File("schemaname"), getResourceFile("valid.xml"));
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals("should catch IOException", e.getCause().getClass(), IOException.class);
        }

    }

    @Test
    public void shouldCatchFatalException() throws IOException, SAXException {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
        }});
        XmlValidator validator = new DefaultXmlValidator(logger);
        try {
            validator.validate(getResourceFile("invalid_schema.xsd"), getResourceFile("valid.xml"));
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals("should catch Sax exception", e.getCause().getClass(), SAXParseException.class);
        }
    }

    @Test
    public void shouldReturnSourceNotFoundError() throws IOException, SAXException {
        XmlValidator validator = new DefaultXmlValidator(logger);
        try {
            validator.validate(getResourceFile("schema.xsd"), new File("dummy.xml"));
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals("should catch IOException", e.getCause().getClass(), IOException.class);
        }

    }

    @Test
    public void shouldFormatMessageWithLocationInfo() {
        assertEquals("Line 1, column 1 : errormessage",
                LoggingErrorHandler.createMessage(new SAXParseException("errormessage", "", "", 1, 1)));
    }

    @Test
    public void shouldLogValidationError() throws SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).error(with(equal("Line 1, column 1 : errormessage")));
        }});

        LoggingErrorHandler errorLogger = new LoggingErrorHandler(logger);
        errorLogger.error(new SAXParseException("errormessage", "", "", 1, 1));
    }

    @Test
    public void shouldLogValidationWarning() throws SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).warn(with(equal("Line 1, column 1 : warning")));
        }});

        LoggingErrorHandler errorLogger = new LoggingErrorHandler(logger);
        errorLogger.warning(new SAXParseException("warning", "", "", 1, 1));
    }

    @Test
    public void shouldReThrowFatalError() throws SAXException {
        try {
            LoggingErrorHandler errorLogger = new LoggingErrorHandler(logger);
            errorLogger.fatalError(new SAXParseException("warning", "", "", 1, 1));
            fail("should throw exception");
        } catch (Exception e) {
            assertEquals("should catch SAXParseException", e.getClass(), SAXParseException.class);
        }
    }

}
