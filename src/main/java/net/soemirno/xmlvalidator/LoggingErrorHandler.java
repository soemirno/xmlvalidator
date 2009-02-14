package net.soemirno.xmlvalidator;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static java.text.MessageFormat.format;

/**
 * Responsible for logging validation errors.
 */
public class LoggingErrorHandler implements ErrorHandler {
    private final Logger logger;

    LoggingErrorHandler(final Logger aLogger) {
        this.logger = aLogger;
    }

    public void warning(final SAXParseException validationError) throws SAXException {
        this.logger.warn(createMessage(validationError));
    }

    public void error(final SAXParseException validationError) throws SAXException {
        this.logger.error(createMessage(validationError));
    }

    public void fatalError(final SAXParseException validationError) throws SAXException {
        throw validationError;
    }

    static String createMessage(final SAXParseException exception) {
        return format(
                "Line {0,number,integer}, column {1,number,integer} : {2}",
                exception.getLineNumber(),
                exception.getColumnNumber(),
                exception.getMessage());
    }

}
