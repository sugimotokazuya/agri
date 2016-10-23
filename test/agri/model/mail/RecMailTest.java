package agri.model.mail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class RecMailTest extends AppEngineTestCase {

    private RecMail model = new RecMail();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
