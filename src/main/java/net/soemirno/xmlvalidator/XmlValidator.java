package net.soemirno.xmlvalidator;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Validates xml files based upon schema.
 */
public interface XmlValidator {
    /**
     * Wrapper for exceptions thrown in XmlValidator.
     */
    class XmlValidatorException extends RuntimeException {
        XmlValidatorException(final Throwable throwable) {
            super(throwable);
        }
    }

    void validate(File schema, File xml) throws IOException, SAXException;
}
