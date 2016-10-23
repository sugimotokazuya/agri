package agri.controller.hanbai;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.service.Const;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class IndexController extends BaseController {

    private ShukkaService service = new ShukkaService();
    private TorihikisakiService tService = new TorihikisakiService();
    private static String YEAR = "/hanbai/year";
    private static String MONTH = "/hanbai/month";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    protected Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        Integer yearI = asInteger("year") == null ? (Integer) sessionScope(YEAR) : asInteger("year");
        if(yearI == null) yearI = year;
        Integer month = asInteger("month") == null ? (Integer) sessionScope(MONTH) : asInteger("month");
        if(month == null) month = cal.get(Calendar.MONTH);
        sessionScope(YEAR, yearI);
        sessionScope(MONTH, month);
        
        Key tKey = null;
        if(asString("torihikisakiRef") != null) {
            tKey = "".equals(asString("torihikisakiRef")) ? null : asKey("torihikisakiRef");
            sessionScope("torihikisakiRef", asString("torihikisakiRef"));
        } else {
            
            tKey = "".equals(sessionScope("torihikisakiRef")) ||
                    sessionScope("torihikisakiRef") == null
                    ? null : KeyFactory.stringToKey((String)sessionScope("torihikisakiRef"));
        }
        
        if(tKey != null) {
            Torihikisaki t = tService.get(tKey);
            if(!loginYago.equals(t.getYagoRef().getModel())) {
                return redirect(basePath);
            }
        }
        
        sessionScope(YEAR, yearI);
        requestScope("torihikisakiRef", sessionScope("torihikisakiRef"));
        requestScope("year", yearI);
        requestScope("month", month);
        requestScope("list", service.getDtoList(
            loginYago, yearI, month, tKey));
        requestScope("tList", tService.getAll(loginYago));
        requestScope("taxConst", Const.TAX);
        
        Calendar today = Util.getCalendar();
        requestScope("todayYear", today.get(Calendar.YEAR));
        requestScope("todayMonth", today.get(Calendar.MONTH) + 1);
        requestScope("todayDay", today.get(Calendar.DAY_OF_MONTH));
        
        return forward("index.jsp");
    }

}
