package agri.controller.hanbai;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Const;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.ShukkaService;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Key;

public class ShukeiController extends BaseController {

    private ShukkaService service = new ShukkaService();
    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    private OneNoticeService onService = new OneNoticeService();

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
        
        Integer yearI = asInteger("year");
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        Key tKey = null;
        if(asString("torihikisakiRef") != "") {
            tKey = asKey("torihikisakiRef");
        }
        
        requestScope("year", yearI);
        requestScope("map", service.getShukeiMap(
            loginYago, yearI == 0 ? null : yearI, tKey));
        requestScope("tList", tService.getAll(loginYago));
        requestScope("hList", hService.getAll(loginYago));
        requestScope("taxConst", Const.TAX);
        requestScope("ukSum", onService.getSumUriageKingaku(loginYago, yearI, tKey));
        
        return forward("shukei.jsp");
    }

}
