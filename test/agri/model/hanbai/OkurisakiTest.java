package agri.model.hanbai;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class OkurisakiTest extends AppEngineTestCase {

    private Okurisaki model = new Okurisaki();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
