package agri.controller.hanbai.kakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.KakudukeSum;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;
import agri.service.hanbai.KakudukeSumService;

public class IndexController extends BaseController {
   
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    private KakudukeService kService = new KakudukeService();
    private KakudukeSumService ksService = new KakudukeSumService();
    private static String YEAR = "/hanbai/kakuduke/year";
    private static String MONTH = "/hanbai/kakuduke/month";
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        if(sessionScope(YEAR) == null) sessionScope(YEAR, year);
        int yearCount = year - kService.getMinYear(loginYago) + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        int yearI = asInteger("year") == null ? (int) sessionScope(YEAR) : asInteger("year");
        Integer month = asInteger("month") == null ? (Integer) sessionScope(MONTH) : asInteger("month");
        if(month == null) month = cal.get(Calendar.MONTH);
        sessionScope(YEAR, yearI);
        sessionScope(MONTH, month);
        requestScope("year", yearI);
        requestScope("month", month);
        
        if(request.getAttribute("sum") != null) {
            return forward("shukei");
        }
        
        if(request.getAttribute("pdf") != null) {
            return forward("pdf");
        }
        
        List<Kakuduke> list = kService.getList(yearI,month, loginYago);
        requestScope("list", list);
        
        // 前月残取得
        cal.set(Calendar.YEAR, yearI);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, -1);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        KakudukeSum ks = ksService.get(year, month, loginYago);
        if(ks == null) kService.shukei(loginYago, year, month);
        
        requestScope("zenZan", ks == null ? 0 : ks.getSum());
        cal.add(Calendar.MONTH, 1);
        requestScope("startDate", cal.getTime());
        
        
        return forward("index.jsp");
    }
}
