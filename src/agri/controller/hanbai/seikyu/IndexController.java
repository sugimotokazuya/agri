package agri.controller.hanbai.seikyu;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.SeikyuService;

import com.google.appengine.api.datastore.Key;

public class IndexController extends BaseController {

    private SeikyuService service = new SeikyuService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSeikyuView();
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
        
        Integer yearI = asInteger("year");
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        Key tKey = null;
        if(asString("torihikisakiRef") != "") {
            tKey = asKey("torihikisakiRef");
        }
        
        requestScope("year", yearI);
        requestScope("list", service.getList(
            loginYago, yearI == 0 ? null : yearI, tKey));
        requestScope("tList", tService.getAll(loginYago));
        return forward("index.jsp");
    }

}
