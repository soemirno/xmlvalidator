package net.soemirno.xmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import static java.text.MessageFormat.format;

/**
 * Main class responsible for starting application.
 */
public class App implements XmlValidator {

    private static XmlValidator validator = new App();
    private static Logger logger = LoggerFactory.getLogger(XmlValidator.class);

    /**
     * Wrapper for exceptions thrown in XmlValidator.
     */
    static class XmlValidatorException extends RuntimeException {
        XmlValidatorException(final Throwable throwable) {
            super(throwable);
        }
    }

    /**
     * Responsible for logging validation errors.
     */
    static class ValidationErrorLogger implements ErrorHandler {

        public void warning(final SAXParseException validationError) throws SAXException {
            logger.warn(createMessage(validationError));
        }

        public void error(final SAXParseException validationError) throws SAXException {
            logger.error(createMessage(validationError));
        }

        public void fatalError(final SAXParseException validationError) throws SAXException {
            throw new XmlValidatorException(validationError);
        }
    }

    public static void main(final String[] args) {
        try {
            validator.validate(new File(args[0]), new File(args[1]));
        } catch (XmlValidatorException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void validate(final File schema, final File xml) {
        assertFilesExist(schema, xml);
        logger.info(format("validating {0} with {1}", xml.getName(), schema.getName()));
        validate(createSchemaValidator(schema), xml);
        logger.info(format("finished validating {0} with {1}", xml.getName(), schema.getName()));
    }

    static void assertFilesExist(final File schema, final File xml) {
        if (!schema.exists()) {
            throw new XmlValidatorException(
                    new IOException(format("Schemafile {0} not found.", schema.getName())));
        }
        if (!xml.exists()) {
            throw new XmlValidatorException(
                    new IOException(format("Sourcefile {0} not found.", xml.getName())));
        }
    }

    static void validate(final Validator aValidator, final File xml) {
        try {
            aValidator.setErrorHandler(new ValidationErrorLogger());
            aValidator.validate(new StreamSource(xml));
        } catch (SAXException e) {
            throw new XmlValidatorException(e);
        } catch (IOException e) {
            throw new XmlValidatorException(e);
        }
    }

    static Validator createSchemaValidator(final File schema) {
        final SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        try {
            return factory.newSchema(new StreamSource(schema)).newValidator();
        } catch (SAXException e) {
            throw new App.XmlValidatorException(e);
        }
    }

    static void setValidator(final XmlValidator aValidator) {
        validator = aValidator;
    }

    static void setLogger(final Logger aLogger) {
        logger = aLogger;
    }

    static String createMessage(final SAXParseException exception) {
        return format(
                "Line {0,number,integer}, column {1,number,integer} : {2}",
                exception.getLineNumber(),
                exception.getColumnNumber(),
                exception.getMessage());
    }

}
