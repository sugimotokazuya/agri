package agri.controller.kintai;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Dakoku;
import agri.model.kintai.ShuseiShinsei;
import agri.service.UserService;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.ShuseiShinseiService;

import com.google.appengine.api.datastore.KeyFactory;

public class ShinseiController extends BaseController {

    private DakokuService service = new DakokuService();
    private UserService userService = new UserService();
    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    
    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return redirect("./");
        }
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(Calendar.YEAR, asInteger("year"));
        cal.set(Calendar.MONTH, asInteger("month"));
        cal.set(Calendar.DAY_OF_MONTH, asInteger("day"));
        
        User user = userService.get(asKey("userKey"));
        
        Dakoku dakoku = service.get(user.getKey(), cal.getTime());
        requestScope("dakoku", dakoku);
        requestScope("user", user);
        
        int status = ssService.createStatus(asString("status"));
        
        ShuseiShinsei ss = ssService.get(user, cal.getTime(), status);
        if(ss != null) {
            cal = Util.getCalendar();
            if(ss.getTime() != null) {
                // 残業申請のときはnull
                cal.setTime(ss.getTime());
                requestScope("time1", cal.get(Calendar.HOUR_OF_DAY));
                requestScope("time2", cal.get(Calendar.MINUTE));
            }
            requestScope("riyu", ss.getRiyu());
            requestScope("key", KeyFactory.keyToString(ss.getKey()));
            requestScope("version", ss.getVersion());
        }
        
        if(status == 5) {
            return forward("zangyo.jsp");
        } else {
            return forward("shinsei.jsp");
        }
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("status", v.required(), v.maxlength(6));
        v.add("year", v.required(), v.maxlength(4), v.integerType());
        v.add("month", v.required(), v.longRange(0, 11), v.maxlength(2));
        v.add("day", v.required(), v.longRange(1, 31), v.maxlength(2));
        v.add("userKey", v.required(), v.maxlength(50));
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        User user = userService.get(asKey("userKey"));
        if(!user.equals(loginUser) && !loginUser.isAuthKintai()) return false;
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
    }
}
