package agri.model.mail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import agri.model.hanbai.UriageCount;

public class UriageCountTest extends AppEngineTestCase {

    private UriageCount model = new UriageCount();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
