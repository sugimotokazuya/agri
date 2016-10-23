package agri.controller.sagyo.sakuduke;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

public class IndexController extends BaseController {

    private SakudukeService service = new SakudukeService();
    private static String YEAR = "/sagyo/sakuduke/year";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        Integer yearI = asInteger("year");
        if(yearI == null && sessionScope(YEAR) != null) {
            yearI = sessionScope(YEAR);
        }
        sessionScope(YEAR, yearI);
        
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        requestScope("year", yearI);
        requestScope("list", service.getList(yearI, loginYago));

        return forward("index.jsp");
    }
}
