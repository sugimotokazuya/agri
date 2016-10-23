package agri.controller.oshirase;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;

public class CreateController extends BaseController {

    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        return forward("create.jsp");
    }
}
