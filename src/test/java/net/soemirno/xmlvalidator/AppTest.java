package net.soemirno.xmlvalidator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import java.io.File;

/**
 * Unit test for simple App.
 */
@RunWith(JMock.class)
public class AppTest {

    Mockery context = new JUnit4Mockery();
    private XmlValidator validator;
    private Logger logger;

    @Before
    public void setUp() {
        validator = context.mock(XmlValidator.class);
        logger = context.mock(Logger.class);
        App.setValidator(validator);
        App.setLogger(logger);
    }

    @Test
    public void shouldCallValidatorWithArgValues() {

        context.checking(new Expectations() {{
            oneOf(validator).validate(with(equal(new File("schemaname"))), with(equal(new File("xmlfile"))));
        }});

        App.main(new String[]{"schemaname", "xmlfile"});
    }

    @Test
    public void shouldLogValidationStartAndEnd() {
        context.checking(new Expectations() {{
            oneOf(logger).info(with(equal("validating valid.xml with schema.xsd")));
            oneOf(logger).info(with(equal("finished validating valid.xml with schema.xsd")));
        }});
        XmlValidator validator = new App();
        validator.validate(getResourceFile("schema.xsd"), getResourceFile("valid.xml"));
    }

    private File getResourceFile(String filename) {
        return new File(this.getClass().getClassLoader().getResource(filename).getFile());
    }

    @Test
    public void shouldReturnSchemaNotFoundError() {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
            oneOf(logger).error("Schemafile schemaname not found.");
        }});

        XmlValidator validator = new App();
        validator.validate(new File("schemaname"), getResourceFile("valid.xml"));
    }

    @Test
    public void shouldReturnSourceNotFoundError() {
        context.checking(new Expectations() {{
            allowing(logger).info(with(any(String.class)));
            oneOf(logger).error("Sourcefile xmlfile not found.");
        }});

        XmlValidator validator = new App();
        validator.validate(getResourceFile("schema.xsd"), new File("xmlfile"));
    }

}
