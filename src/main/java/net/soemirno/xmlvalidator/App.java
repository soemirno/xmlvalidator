package net.soemirno.xmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Responsible for starting application.
 */
public class App implements XmlValidator {

    private static XmlValidator validator;
    private static Logger logger = LoggerFactory.getLogger(XmlValidator.class);

    static {
        validator = new App();
    }

    public static void main(String[] args) {
        validator.validate(new File(args[0]), new File(args[1]));
    }

    static void setValidator(XmlValidator aValidator) {
        validator = aValidator;
    }

    static void setLogger(Logger aLogger) {
        logger = aLogger;
    }

    public void validate(File schema, File xml) {
        logger.info("validating " + xml.getName() + " with " + schema.getName());
    }

}
