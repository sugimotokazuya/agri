package agri.model.mail;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import agri.model.hanbai.UriageKingaku;

public class UriageKingakuTest extends AppEngineTestCase {

    private UriageKingaku model = new UriageKingaku();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
