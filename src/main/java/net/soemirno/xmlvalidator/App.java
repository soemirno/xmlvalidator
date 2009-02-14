package net.soemirno.xmlvalidator;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Main class responsible for starting application.
 */
public final class App {

    private App() {
    }

    private static XmlValidator validator = new DefaultXmlValidator();


    public static void main(final String[] args) throws IOException, SAXException {
        validator.validate(new File(args[0]), new File(args[1]));
    }

    static void setValidator(final XmlValidator aValidator) {
        validator = aValidator;
    }

}
