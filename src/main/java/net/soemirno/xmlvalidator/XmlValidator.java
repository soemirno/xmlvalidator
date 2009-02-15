package net.soemirno.xmlvalidator;

import java.io.File;

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

    void validate(File xml);
}
