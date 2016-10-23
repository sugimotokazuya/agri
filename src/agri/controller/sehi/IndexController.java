package agri.controller.sehi;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;

public class IndexController extends BaseController {

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiView();
    }
    
    @Override
    protected Navigation run() throws Exception {
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        return forward("index.jsp");
    }

}
