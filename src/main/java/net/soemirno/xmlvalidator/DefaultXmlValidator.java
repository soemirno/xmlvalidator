package net.soemirno.xmlvalidator;

import static net.soemirno.xmlvalidator.Util.createValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import static java.text.MessageFormat.format;

/**
 * Default xml validator.
 */
public class DefaultXmlValidator implements XmlValidator {

    private Logger logger;

    DefaultXmlValidator(final Logger defaultLogger) {
        this.logger = defaultLogger;
    }

    public DefaultXmlValidator() {
        this(LoggerFactory.getLogger(DefaultXmlValidator.class));
    }

    public void validate(final File schema, final File xml) throws IOException, SAXException {
        Util.assertFilesExist(schema, xml);
        logger.info(format("validating {0} with {1}", xml.getName(), schema.getName()));
        final Validator validator = createValidator(schema, new LoggingErrorHandler(logger));
        validator.validate(new StreamSource(xml));
        logger.info(format("finished validating {0} with {1}", xml.getName(), schema.getName()));
    }

}
