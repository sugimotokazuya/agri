package agri.controller.shinkoku;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class ResetShukeiControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/shinkoku/resetShukei");
        ResetShukeiController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/shinkoku/resetShukei.jsp"));
    }
}
