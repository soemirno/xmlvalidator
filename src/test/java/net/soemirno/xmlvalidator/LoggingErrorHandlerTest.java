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

@RunWith(JMock.class)
public class LoggingErrorHandlerTest {

    Mockery context = new JUnit4Mockery();

    private Logger logger;

    @Before
    public void setUp() {
        logger = context.mock(Logger.class);
    }

    @Test
    public void shouldFormatMessageWithLocationInfo() {
        assertEquals("Line 1, column 1 : errormessage",
                LoggingErrorHandler.createMessage(new SAXParseException("errormessage", "", "", 1, 1)));
    }

    @Test
    public void shouldFormatValidationError() throws SAXException {
        context.checking(new Expectations() {{
            oneOf(logger).error(with(equal("Line 1, column 1 : errormessage")));
        }});

        LoggingErrorHandler errorLogger = new LoggingErrorHandler(logger);
        errorLogger.error(new SAXParseException("errormessage", "", "", 1, 1));
    }

    @Test
    public void shouldFormatValidationWarning() throws SAXException {
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
