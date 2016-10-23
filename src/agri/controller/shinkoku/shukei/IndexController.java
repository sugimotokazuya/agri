package agri.controller.shinkoku.shukei;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;

public class IndexController extends BaseController {

    private KamokuService service = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Calendar cal = Util.getCalendar();
        List<Integer> yearList = new ArrayList<Integer>();
        for(int i=cal.get(Calendar.YEAR); i>= 2010; --i)
        {
            yearList.add(i);
        }
        requestScope("yearList", yearList);
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("kamokuList", service.getAll(loginYago));
        return forward("index.jsp");
    }
}
