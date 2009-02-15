package net.soemirno.xmlvalidator;

import java.io.File;

/**
 * Creates validator.
 */
public interface XmlValidatorFactory {
    XmlValidator createValidator(File schema);
}
