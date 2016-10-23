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

public class ZangyoShinseiController extends BaseController {

    private UserService userService = new UserService();
    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    private DakokuService dService = new DakokuService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            Calendar cal = Util.getCalendar();
            Util.resetTime(cal);
            cal.set(Calendar.YEAR, asInteger("year"));
            cal.set(Calendar.MONTH, asInteger("month"));
            cal.set(Calendar.DAY_OF_MONTH, asInteger("day"));
            
            User user = userService.get(asKey("userKey"));
            
            Dakoku dakoku = dService.get(user.getKey(), cal.getTime());
            requestScope("dakoku", dakoku);
            requestScope("user", user);
            return forward("zangyo.jsp");
        }
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(Calendar.YEAR, asInteger("year"));
        cal.set(Calendar.MONTH, asInteger("month"));
        cal.set(Calendar.DAY_OF_MONTH, asInteger("day"));
        
        User user = userService.get(asKey("userKey"));
        
        ShuseiShinsei ss;
        if(!Util.isEmpty(asString("key"))) {
            ss = ssService.get(asKey("key"), asLong("version"));
        } else {
            ss = new ShuseiShinsei();
        }
        ss.getYagoRef().setModel(loginYago);
        ss.getUserRef().setModel(user);
        ss.setDate(cal.getTime());
        ss.setStatus(5);
        ss.setRiyu(asString("riyu"));
        ss.setShonin(0);
        
        if(!Util.isEmpty(asString("key"))) {
            ssService.update(ss);
        } else {
            ssService.insert(ss);
        }
        
        return redirect("/kintai");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("year", v.required(), v.maxlength(4), v.integerType());
        v.add("month", v.required(), v.longRange(0, 11), v.maxlength(2));
        v.add("day", v.required(), v.longRange(1, 31), v.maxlength(2));
        v.add("userKey", v.required(), v.maxlength(50));
        v.add("riyu", v.required(), v.maxlength(100));
        v.add("key", v.maxlength(50));
        v.add("version", v.longType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        User user = userService.get(asKey("userKey"));
        if(!user.equals(loginUser) && !loginUser.isAuthKintai()) return false;
        return true;
    }
}
