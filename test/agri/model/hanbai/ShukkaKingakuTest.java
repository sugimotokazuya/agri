package agri.model.hanbai;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class ShukkaKingakuTest extends AppEngineTestCase {

    private ShukkaKingaku model = new ShukkaKingaku();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
