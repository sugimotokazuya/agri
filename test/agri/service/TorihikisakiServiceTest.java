package agri.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class TorihikisakiServiceTest extends AppEngineTestCase {

    private TorihikisakiService service = new TorihikisakiService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
