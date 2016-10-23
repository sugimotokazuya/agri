package agri.model.sehi;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class SekkeiTest extends AppEngineTestCase {

    private Sekkei model = new Sekkei();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
