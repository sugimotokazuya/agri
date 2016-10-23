package agri.controller.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.mail.RecMail;
import agri.service.Util;
import agri.service.mail.RecMailService;

public class IndexController extends BaseController {

    private RecMailService service = new RecMailService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthMailView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        int minYear = cal.get(Calendar.YEAR);
        
        int year = asInteger("year") == null ? cal.get(Calendar.YEAR) : asInteger("year");
        int month = asInteger("month") == null ? cal.get(Calendar.MONTH) + 1 : asInteger("month");
        requestScope("year", year);
        requestScope("month", month);
        
        RecMail minRm = service.getByMinDate(loginYago);
        if(minRm != null) {
            Calendar minCal = Util.getCalendar();
            minCal.setTime(minRm.getDate());
            minYear = minCal.get(Calendar.YEAR);
        }
        
        Calendar calY = Util.getCalendar();
        List<Integer> yearList = new ArrayList<Integer>();
        for(int i=calY.get(Calendar.YEAR); i>= minYear; --i)
        {
            yearList.add(i);
        }
        requestScope("yearList", yearList);
        
        List<Integer> monthList = new ArrayList<Integer>();
        for(int i = 1; i < 13; ++i) {
            monthList.add(i);
        }
        requestScope("monthList", monthList);
        
        Calendar start = Util.getCalendar();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTimeInMillis(start.getTimeInMillis());
        end.set(Calendar.MONTH, start.get(Calendar.MONTH) + 1);
        
        List<RecMail> list = service.getListByDateRange(
            loginYago, start.getTime(), end.getTime());
        requestScope("list", list);
        
        return forward("index.jsp");
    }
}
