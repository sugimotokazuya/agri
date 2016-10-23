package agri.model.hanbai;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class SeikyuRecTest extends AppEngineTestCase {

    private SeikyuRec model = new SeikyuRec();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
