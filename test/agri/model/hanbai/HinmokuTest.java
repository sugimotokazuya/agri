package agri.model.hanbai;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import agri.model.Hinmoku;

public class HinmokuTest extends AppEngineTestCase {

    private Hinmoku model = new Hinmoku();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
