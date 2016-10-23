package agri.model.hanbai;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class ShukkaCountTest extends AppEngineTestCase {

    private ShukkaCount model = new ShukkaCount();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
