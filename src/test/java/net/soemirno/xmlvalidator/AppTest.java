package net.soemirno.xmlvalidator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Unit test for simple App.
 */
@RunWith(JMock.class)
public class AppTest {

    Mockery context = new JUnit4Mockery();
    private XmlValidator validator;

    @Before
    public void setUp() {
        validator = context.mock(XmlValidator.class);
        App.setValidator(validator);
    }

    @Test
    public void shouldCallValidatorWithArgValues() {

        context.checking(new Expectations() {{
            oneOf(validator).validate(with(equal(new File("schemaname"))), with(equal(new File("xmlfile"))));
        }});

        App.main(new String[]{"schemaname", "xmlfile"});
    }
}
