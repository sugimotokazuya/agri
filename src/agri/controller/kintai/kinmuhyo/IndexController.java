package agri.controller.kintai.kinmuhyo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.kintai.Kinmuhyo;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.KinmuhyoService;

public class IndexController extends BaseController {

    private KinmuhyoService kService = new KinmuhyoService();
    private DakokuService dakokuService = new DakokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai() || loginUser.isUseDakoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        String SES_STR = "/kintai/kinmuhyo/YEAR";
        if(asInteger("year") != null) {
            sessionScope(SES_STR, asInteger("year"));
        }
        Integer year = (Integer)sessionScope(SES_STR);
        requestScope("year", year);
        
        Date[] calRange = new Date[2];
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        if(year == null) year = cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        calRange[0] = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        calRange[1] = cal.getTime();
        
        List<Kinmuhyo> list;
        int minYear;
        if(loginUser.isAuthKintai()) {
            list = kService.getList(loginYago, calRange[0], calRange[1]);
            minYear = dakokuService.getMinYear();
        } else {
            list = kService.getList(loginUser, calRange[0], calRange[1]);
            minYear = dakokuService.getMinYear(loginUser);
        }
        requestScope("list", list);
        requestScope("years", Util.createYearRange(minYear));
        
        return forward("index.jsp");
    }
}
