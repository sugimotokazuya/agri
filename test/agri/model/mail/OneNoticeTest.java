package agri.model.mail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class OneNoticeTest extends AppEngineTestCase {

    private OneNotice model = new OneNotice();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
