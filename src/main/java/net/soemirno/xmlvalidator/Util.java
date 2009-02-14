package net.soemirno.xmlvalidator;

import org.xml.sax.SAXException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Util class.
 */
public final class Util {

    private Util() {
    }

    static void assertFilesExist(final File schema, final File xml) {
        if (!schema.exists()) {
            throw new XmlValidator.XmlValidatorException(
                    new IOException(
                            MessageFormat.format("Schemafile {0} not found.", schema.getName())));
        }
        if (!xml.exists()) {
            throw new XmlValidator.XmlValidatorException(
                    new IOException(
                            MessageFormat.format("Sourcefile {0} not found.", xml.getName())));
        }
    }

    static Validator createValidator(final File schema, final LoggingErrorHandler errorHandler) {
        final SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        try {
            final Validator validator = factory.newSchema(new StreamSource(schema)).newValidator();
            validator.setErrorHandler(errorHandler);
            return validator;
        } catch (SAXException e) {
            throw new XmlValidator.XmlValidatorException(e);
        }
    }

}
