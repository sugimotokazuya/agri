package agri.controller.kintai.shift;

import java.util.Calendar;
import java.util.Date;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Shift;
import agri.service.Const;
import agri.service.Util;
import agri.service.kintai.ShiftService;

import com.google.appengine.api.datastore.Key;

public class AddUserController extends BaseController {

    private ShiftService shiftService = new ShiftService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("../");
        }
        
        Integer year = asInteger("year");
        Integer month = asInteger("month");
        requestScope("year", year);
        requestScope("month", month);
        Key userKey = asKey("userRef");
        User user = userService.get(userKey);
        Date[] calRange = Util.createOneMonthRange(year, month);
        int count = shiftService.getCount(user, calRange[0], calRange[1]);
        if(count > 0) {
            return forward("./");
        }
        
        Calendar start = Util.getCalendar();
        start.setTime(calRange[0]);
        Calendar end = Util.getCalendar();
        end.setTime(calRange[1]);
        while(start.getTimeInMillis() < end.getTimeInMillis()) {
            Shift shift = new Shift();
            shift.setDate(start.getTime());
            shift.setYasumi(0);
            shift.getUserRef().setModel(user);
            shift.getYagoRef().setModel(user.getYagoRef().getModel());
            shiftService.insert(shift);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        
        return redirect("./");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR), cal.get(Calendar.YEAR) + 1, "本年と翌年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("userRef", v.required(), v.maxlength(Const.KEY_LENGTH));
        return v.validate();
    }
}
