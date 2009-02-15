package net.soemirno.xmlvalidator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
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
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(JMock.class)
public class DefaultXmlValidatorTest extends AbstractValidatorTestCase {

    Mockery context = new JUnit4Mockery();

    private Logger logger;

    @Before
    public void setUp() {
        logger = context.mock(Logger.class);
    }

    @Test
    public void shouldLogValidationStartAndEnd() throws IOException, SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).info(with(equal("validator created with schema.xsd.")));
            oneOf(logger).info(with(equal("validating valid.xml.")));
            oneOf(logger).info(with(equal("finished validating valid.xml.")));
        }});
        XmlValidator validator = new DefaultXmlValidator(getResourceFile("schema.xsd"), logger);
        validator.validate(getResourceFile("valid.xml"));
    }

    @Test
    public void shouldReturnSchemaNotFoundError() throws IOException, SAXException {
        try {
            new DefaultXmlValidator(new File("schemaname"), logger);
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals("should catch IOException", e.getCause().getClass(), SAXParseException.class);
        }

    }

    @Test
    public void shouldCreateValidator() throws IOException, SAXException {
        XmlValidator xmlValidator = new DefaultXmlValidator(getResourceFile("schema.xsd"));
        assertNotNull(xmlValidator);
    }

    @Test
    public void shouldCatchFatalException() throws IOException, SAXException {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
        }});
        try {
            new DefaultXmlValidator(getResourceFile("invalid_schema.xsd"), logger);
            fail("should throw exception");
        } catch (XmlValidator.XmlValidatorException e) {
            assertEquals("should catch Sax exception", e.getCause().getClass(), SAXParseException.class);
        }
    }

    @Test
    public void shouldReturnSourceNotFoundError() throws IOException, SAXException {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
            oneOf(logger).error(with(any(String.class)), with(any(FileNotFoundException.class)));
        }});
        XmlValidator validator = new DefaultXmlValidator(getResourceFile("schema.xsd"), logger);
        validator.validate(new File("dummy.xml"));
    }

    @Test
    public void shouldLogValidationError() throws IOException, SAXException {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
            allowing(logger).error(with(any(String.class)));
        }});
        XmlValidator validator = new DefaultXmlValidator(getResourceFile("schema.xsd"), logger);
        validator.validate(getResourceFile("invalid.xml"));
    }

    @Test
    public void shouldCatchSAXParseException() throws IOException, SAXException {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
            oneOf(logger).error(with(any(String.class)), with(any(SAXParseException.class)));
        }});
        XmlValidator validator = new DefaultXmlValidator(getResourceFile("schema.xsd"), logger, null);
        validator.validate(getResourceFile("invalid.xml"));
    }

}
