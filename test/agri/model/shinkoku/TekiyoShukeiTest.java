package agri.model.shinkoku;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class TekiyoShukeiTest extends AppEngineTestCase {

    private TekiyoShukei model = new TekiyoShukei();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
