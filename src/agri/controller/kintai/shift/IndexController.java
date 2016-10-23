package agri.controller.kintai.shift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Shift;
import agri.service.UserService;
import agri.service.Util;
import agri.service.kintai.ShiftService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class IndexController extends BaseController {

    private UserService userService = new UserService();
    private ShiftService shiftService = new ShiftService();
    private static String YEAR = "/kintai/shift/year";
    private static String MONTH = "/kintai/shift/month";
    private static String USER = "/kintai/shift/user";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isUseDakoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Integer year = asInteger("year");
        Integer month = asInteger("month");
        Date[] calRange;
        
        if(year == null || month == null) {
            if(sessionScope(YEAR) != null) {
                year = sessionScope(YEAR);
                month = sessionScope(MONTH);
                calRange = Util.createOneMonthRange(year, month);
            } else {
                calRange = Util.createOneMonthRange();
                Calendar cal = Util.getCalendar();
                cal.setTime(calRange[0]);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
            }
        } else {
            calRange = Util.createOneMonthRange(year, month);
            sessionScope(YEAR, year);
            sessionScope(MONTH, month);
        }
        requestScope("year", year);
        requestScope("month", month);
        
        User user;
        Key userKey = asKey("userRef");
        if(userKey != null) {
            user = userService.get(userKey);
            sessionScope(USER, user);
        } else if(sessionScope(USER) != null) {
            user = sessionScope(USER);
        } else {
            user = loginUser;
        }
        if(loginUser.isAuthKintai()) {
            List<User> userList = userService.getAll(loginYago);
            requestScope("userList", userList);
        }
        requestScope("user", user);
        requestScope("userRef", KeyFactory.keyToString(user.getKey()));
        
        Calendar cal = Util.getCalendar();
        int minYear = cal.get(Calendar.YEAR) - 1;
        requestScope("years", Util.createYearRange(minYear));

        Calendar start = Util.getCalendar();
        start.setTime(calRange[0]);
        Calendar end = Util.getCalendar();
        end.setTime(calRange[1]);
        
        List<Integer> dayList = new ArrayList<Integer>();
        switch(start.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY :dayList.add(null);
        case Calendar.SATURDAY :dayList.add(null);
        case Calendar.FRIDAY :dayList.add(null);
        case Calendar.THURSDAY :dayList.add(null);
        case Calendar.WEDNESDAY :dayList.add(null);
        case Calendar.TUESDAY :dayList.add(null);
        
        }
        
        Map<Integer, List<Shift>> shiftMap = new HashMap<Integer, List<Shift>>();
        Map<Integer, Shift> myShiftMap = new HashMap<Integer, Shift>();
               
        while(start.getTimeInMillis() < end.getTimeInMillis()) {
            Integer day = start.get(Calendar.DAY_OF_MONTH);
            dayList.add(day);
            Shift shift = shiftService.get(user, start.getTime());
            myShiftMap.put(day, shift);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<Shift> list = shiftService.getList(loginYago, calRange[0], calRange[1]);
        
        for(Shift shift : list) {
            cal = Util.getCalendar();
            cal.setTime(shift.getDate());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            if(!shiftMap.containsKey(day)) {
                shiftMap.put(day, new ArrayList<Shift>());
            }
            List<Shift> shiftList = shiftMap.get(day);
            shiftList.add(shift);
        }
        
        requestScope("dayList", dayList);
        requestScope("shiftMap", shiftMap);
        requestScope("myShiftMap", myShiftMap);
        
        return forward("index.jsp");
    }
    
}
