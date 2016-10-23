package agri.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class UserTest extends AppEngineTestCase {

    private User model = new User();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
