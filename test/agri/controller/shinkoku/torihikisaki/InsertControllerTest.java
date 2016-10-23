package agri.controller.shinkoku.torihikisaki;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import agri.controller.torihikisaki.InsertController;

public class InsertControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/shinkoku/torihikisaki/insert");
        InsertController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/shinkoku/torihikisaki/insert.jsp"));
    }
}
