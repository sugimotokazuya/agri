package agri.controller.kintai.shift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.kintai.Shift;
import agri.service.Util;
import agri.service.kintai.ShiftService;

public class DakokuShiftController extends BaseController {

    private ShiftService shiftService = new ShiftService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.equals(loginYago.getKintaiRef().getModel())
                || loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        int amount = 0;
        switch(start.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY :amount--;
        case Calendar.SATURDAY : amount--;
        case Calendar.FRIDAY :amount--;
        case Calendar.THURSDAY :amount--;
        case Calendar.WEDNESDAY :amount--;
        case Calendar.TUESDAY :amount--;
        }
        
        start.add(Calendar.DAY_OF_MONTH, amount);
        Calendar end = Util.getCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DAY_OF_MONTH, 35);
        
        List<Date> dayList = new ArrayList<Date>();
        
        Map<Date, List<Shift>> shiftMap = new HashMap<Date, List<Shift>>();
        Calendar cal = Util.getCalendar();
        cal.setTime(start.getTime());

        while(cal.getTimeInMillis() < end.getTimeInMillis()) {
            dayList.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<Shift> list = shiftService.getList(loginYago, start.getTime(), end.getTime());
        
        for(Shift shift : list) {
            cal = Util.getCalendar();
            cal.setTime(shift.getDate());
            Date date = cal.getTime();
            if(!shiftMap.containsKey(date)) {
                shiftMap.put(date, new ArrayList<Shift>());
            }
            List<Shift> shiftList = shiftMap.get(date);
            shiftList.add(shift);
        }
        
        requestScope("dayList", dayList);
        requestScope("shiftMap", shiftMap);
        Calendar today = Util.getCalendar();
        Util.resetTime(today);
        requestScope("today", today.getTime());
        
        return forward("dakokuShift.jsp");
    }
    
}
