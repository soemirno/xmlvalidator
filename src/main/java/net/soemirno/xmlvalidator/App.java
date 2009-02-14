package net.soemirno.xmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Responsible for starting application.
 */
public class App implements XmlValidator {

    private static XmlValidator validator;
    private static Logger logger = LoggerFactory.getLogger(XmlValidator.class);
    private static final MessageFormat ERROR_MESSAGE_FORMAT = new MessageFormat(
            "Line {0,number,integer}, column {1,number,integer} : {2}");


    private class XmlValidatorException extends RuntimeException {
        XmlValidatorException(Throwable throwable) {
            super(throwable);
        }
    }

    private class ValidationErrorLogger implements ErrorHandler {

        private String createMessage(SAXParseException exception) {
            return ERROR_MESSAGE_FORMAT.format(new Object[]{
                    exception.getLineNumber(),
                    exception.getColumnNumber(),
                    exception.getMessage()});
        }

        public void warning(SAXParseException e) throws SAXException {
            logger.warn(createMessage(e));
        }

        public void error(SAXParseException e) throws SAXException {
            logger.error(createMessage(e));
        }

        public void fatalError(SAXParseException e) throws SAXException {
            throw new XmlValidatorException(e);
        }
    }

    static {
        validator = new App();
    }

    public static void main(String[] args) {
        try {
            validator.validate(new File(args[0]), new File(args[1]));
        } catch (XmlValidatorException e) {
            logger.error(e.getMessage(), e);
        }
    }

    static void setValidator(XmlValidator aValidator) {
        validator = aValidator;
    }

    static void setLogger(Logger aLogger) {
        logger = aLogger;
    }

    public void validate(File schema, File xml) {
        if (!schema.exists()) {
            logger.error("Schemafile " + schema.getName() + " not found.");
            return;
        }
        if (!xml.exists()) {
            logger.error("Sourcefile " + xml.getName() + " not found.");
            return;
        }
        logger.info("validating " + xml.getName() + " with " + schema.getName());

        validate(createSchemaValidator(schema), xml);

        logger.info("finished validating " + xml.getName() + " with " + schema.getName());
    }

    private void validate(Validator validator, File xml) {
        try {
            validator.setErrorHandler(new ValidationErrorLogger());
            validator.validate(new StreamSource(xml));
        } catch (SAXException e) {
            throw new XmlValidatorException(e);
        } catch (IOException e) {
            throw new XmlValidatorException(e);
        }
    }

    private Validator createSchemaValidator(File schema) {
        final Source schemaSource = new StreamSource(schema);
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            return schemaFactory.newSchema(schemaSource).newValidator();
        } catch (SAXException e) {
            throw new XmlValidatorException(e);
        }
    }

}
