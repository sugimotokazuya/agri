package agri.model.hanbai;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class KakudukeSumTest extends AppEngineTestCase {

    private KakudukeSum model = new KakudukeSum();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
