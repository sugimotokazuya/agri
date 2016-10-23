package agri.controller.kintai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Dakoku;
import agri.model.kintai.ShuseiShinsei;
import agri.model.kintai.Teiji;
import agri.service.UserService;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.ShuseiShinseiService;
import agri.service.kintai.TeijiService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class IndexController extends BaseController {

    private UserService userService = new UserService();
    private DakokuService dakokuService = new DakokuService();
    private TeijiService teijiService = new TeijiService();
    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    private static String YEAR = "/kintai/year";
    private static String MONTH = "/kintai/month";
    private static String USER = "/kintai/user";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
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
        } else if(sessionScope(USER) != null){
            user = sessionScope(USER);
        } else {
            user = loginUser;
        }
        sessionScope(USER, user);
        if(loginUser.isAuthKintai()) {
            List<User> userList = userService.getAll(loginYago);
            requestScope("userList", userList);
        }
        
        List<Dakoku> list = dakokuService.getList(user, calRange[0], calRange[1]);
        requestScope("user", user);
        requestScope("userRef", KeyFactory.keyToString(user.getKey()));
        int minYear = dakokuService.getMinYear(user);
        requestScope("years", Util.createYearRange(minYear));
        
        Calendar start = Util.getCalendar();
        start.setTime(calRange[0]);
        Calendar end = Util.getCalendar();
        end.setTime(calRange[1]);
        
        List<Integer> dayList = new ArrayList<Integer>();
        Map<Integer, Dakoku> map = new HashMap<Integer, Dakoku>();
        
        List<Teiji> teijiList = teijiService.getByDateRange(
            loginYago, start.getTime(), end.getTime());
        Map<Integer, Teiji> teijiMap = new HashMap<Integer, Teiji>();
        Map<Integer, Date> dateMap = new HashMap<Integer, Date>();
               
        while(start.getTimeInMillis() < end.getTimeInMillis()) {
            dayList.add(start.get(Calendar.DAY_OF_MONTH));
            Teiji teiji = findTeiji(teijiList, start.getTime());
            Integer day = new Integer(start.get(Calendar.DAY_OF_MONTH));
            teijiMap.put(day, teiji);
            dateMap.put(day, start.getTime());
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        requestScope("teijiMap", teijiMap);
                
        Iterator<Dakoku> ite = list.iterator();
        while(ite.hasNext()) {
            Dakoku dakoku = ite.next();
            Calendar cal = Util.getCalendar();
            cal.setTime(dakoku.getDate());
            map.put(new Integer(cal.get(Calendar.DAY_OF_MONTH)), dakoku);
        }
        
        Map<Integer, Map<String, ShuseiShinsei>> ssMap = new HashMap<Integer, Map<String, ShuseiShinsei>>();
        List<ShuseiShinsei> ssList = ssService.getShinseiChuList(user, calRange[0], calRange[1]);
        Iterator<ShuseiShinsei> ssIte = ssList.iterator();
        while(ssIte.hasNext()) {
            ShuseiShinsei ss = ssIte.next();
            Calendar cal = Util.getCalendar();
            cal.setTime(ss.getDate());
            Integer day = new Integer(cal.get(Calendar.DAY_OF_MONTH));
            if(!ssMap.containsKey(day)) {
                ssMap.put(day, new HashMap<String, ShuseiShinsei>());
            }
            Map<String, ShuseiShinsei> ss2Map = ssMap.get(day);
            ss2Map.put(String.valueOf(ss.getStatus()), ss);
        }
        
        requestScope("dayList", dayList);
        requestScope("map", map);
        requestScope("dateMap", dateMap);
        requestScope("ssMap", ssMap);
        requestScope("ssList", ssService.getList(user, calRange[0], calRange[1]));
        
        return forward("index.jsp");
    }
    
    private Teiji findTeiji(List<Teiji> list, Date date) {
        for(Teiji teiji : list) {
            if(date.getTime() >= teiji.getStart().getTime() 
                    && date.getTime() <= teiji.getEnd().getTime()) {
                return teiji;
            }
        }
        return null;
    }
}
