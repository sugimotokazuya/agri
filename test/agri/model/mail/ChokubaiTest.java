package agri.model.mail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import agri.model.Chokubai;

public class ChokubaiTest extends AppEngineTestCase {

    private Chokubai model = new Chokubai();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
