package agri.model.sehi;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class SokuteiTest extends AppEngineTestCase {

    private Sokutei model = new Sokutei();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
