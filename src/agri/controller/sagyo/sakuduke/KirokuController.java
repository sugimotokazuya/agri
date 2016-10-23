package agri.controller.sagyo.sakuduke;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.sagyo.Sakuduke;
import agri.service.Const;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.Key;

public class KirokuController extends BaseController {

    private SakudukeService service = new SakudukeService();
    public static String YEAR = "/sagyo/kiroku/year";
    public static String MONTH = "/sagyo/kiroku/month";
    public static String DAY = "/sagyo/kiroku/day";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeView();
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        return v.validate();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return redirect("/sakuduke");
        }
        
        Key key = asKey("key");
        Sakuduke sakuduke = service.get(key);
        requestScope("sakuduke", sakuduke);
        
        Integer year = asInteger("year");
        Integer month = asInteger("month");
        Integer day = asInteger("day");
        
        if(year == null || month == null || day == null) {
            if(sessionScope(YEAR) != null) {
                year = sessionScope(YEAR);
                month = sessionScope(MONTH);
                day = sessionScope(DAY);
            } else {
                Calendar cal = Util.getCalendar();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);
            }
        }
        
        requestScope("year", year);
        requestScope("month", month);
        requestScope("day", day);
        
        return forward("kiroku.jsp");
    }
}
