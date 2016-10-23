package agri.controller.sehi.sokutei;

import java.util.Calendar;
import java.util.Date;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.HatakeService;
import agri.service.Util;
import agri.service.sehi.SokuteiService;

import com.google.appengine.api.datastore.Key;

public class IndexController extends BaseController {

    private SokuteiService service = new SokuteiService();
    private HatakeService hatakeService = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - service.getMinYear() + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        requestScope("hatakeList", hatakeService.getAll(loginYago));
        
        Date startDate = null;
        Date endDate = null;
        Key hatakeKey = null;
        String y = asString("year");
        if(y == null) y = String.valueOf(year);
        if(!"".equals(y)) {
            Calendar start = Util.getCalendar();
            start.clear();
            start.set(Calendar.YEAR, Integer.parseInt(y));
            start.set(Calendar.MONTH, 0);
            start.set(Calendar.DAY_OF_MONTH, 1);
            Calendar end = Util.getCalendar();
            end.setTimeInMillis(start.getTimeInMillis());
            end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
            startDate = start.getTime();
            endDate = end.getTime();
        }
        
        if(!"".equals(asString("hatake"))) {
            hatakeKey = asKey("hatake");
        }
        
        requestScope("year", y);
        requestScope("sokuteiList", 
            service.get(loginYago, startDate, endDate, hatakeKey));
        return forward("index.jsp");
    }
}
