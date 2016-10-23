package agri.model.sehi;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import agri.model.Hatake;

public class HatakeTest extends AppEngineTestCase {

    private Hatake model = new Hatake();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
