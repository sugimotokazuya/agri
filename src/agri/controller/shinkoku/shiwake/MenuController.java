package agri.controller.shinkoku.shiwake;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class MenuController extends BaseController {

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
        if(sessionScope("shiwakeViewYear") == null) {
            requestScope("year", cal.get(Calendar.YEAR));
        } else {
            requestScope("year", sessionScope("shiwakeViewYear"));
        }
        if(sessionScope("shiwakeViewKamoku") != null) {
            requestScope("kamoku", KeyFactory.keyToString(
                (Key)sessionScope("shiwakeViewKamoku")));
        }
        requestScope("kamokuList", service.getAll(loginYago));
        return forward("menu.jsp");
    }
}
