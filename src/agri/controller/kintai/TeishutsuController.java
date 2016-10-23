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
import agri.model.kintai.Kinmuhyo;
import agri.model.kintai.ShuseiShinsei;
import agri.model.kintai.Teiji;
import agri.service.UserService;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.KinmuhyoService;
import agri.service.kintai.ShuseiShinseiService;
import agri.service.kintai.TeijiService;

import com.google.appengine.api.datastore.KeyFactory;

public class TeishutsuController extends BaseController {

    private UserService userService = new UserService();
    private DakokuService dakokuService = new DakokuService();
    private TeijiService teijiService = new TeijiService();
    private KinmuhyoService kService = new KinmuhyoService();
    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    
    @Override
    public boolean validateAuth() {
        User user;
        if(asString("key") == null) {
            user = userService.get(asKey("userKey"));
        } else {
            Kinmuhyo k = kService.get(asKey("key"), asLong("version"));
            user = k.getUserRef().getModel();
        }
        if(!loginUser.isAuthKintai() && !user.equals(loginUser)) {
            return false;
        }
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        User user;
        if(asString("userKey") != null) {
            user = userService.get(asKey("userKey"));
        } else {
            Kinmuhyo k = kService.get(asKey("key"));
            user = k.getUserRef().getModel();
        }
        requestScope("user", user);
        requestScope("userRef", KeyFactory.keyToString(user.getKey()));
        
        Calendar cal = Util.getCalendar();
        cal.add(Calendar.MONTH, -1);
        int year = cal.get(Calendar.YEAR);
        if(asInteger("year") != null) year = asInteger("year");
        int month = cal.get(Calendar.MONTH);
        if(asInteger("month") != null) month = asInteger("month");
        Date[] calRange = Util.createOneMonthRange(year, month);

        requestScope("year", year);
        requestScope("month", month);
        
        Kinmuhyo kinmuhyo = kService.get(user, calRange[0]);
        if(kinmuhyo == null) {
            kinmuhyo = new Kinmuhyo();
            kinmuhyo.getYagoRef().setModel(loginYago);
            kinmuhyo.getUserRef().setModel(user);
            kinmuhyo.setDate(calRange[0]);
            kinmuhyo.setShonin(0);
            kinmuhyo.setEmployeeType(user.getEmployeeType());
            kService.insert(kinmuhyo);
        }
        
        requestScope("Kinmuhyo", kinmuhyo);
        requestScope("key", KeyFactory.keyToString(kinmuhyo.getKey()));
        requestScope("version", kinmuhyo.getVersion());
        int minYear = dakokuService.getMinYear(user);
        requestScope("years", Util.createYearRange(minYear));
        
        // 0:申請前,1:申請中,2:承認,3:却下
        switch(kinmuhyo.getShonin()) {
        case 1:
        case 2:
            requestScope("errorMessage"
                , kinmuhyo.getShoninStr() + "のため提出すべき勤務表がありません。");
        }
        
        List<Dakoku> list = dakokuService.getList(user, calRange[0], calRange[1]);
        
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
            cal = Util.getCalendar();
            cal.setTime(dakoku.getDate());
            map.put(new Integer(cal.get(Calendar.DAY_OF_MONTH)), dakoku);
            if(dakoku.isYasumi()) requestScope("yasumi" + cal.get(Calendar.DAY_OF_MONTH), "on");
            if(dakoku.isOhiru()) requestScope("ohiru" + cal.get(Calendar.DAY_OF_MONTH), "on");
            
            // 戻りもれ、退勤もれチェック
            if(dakoku.getStart()!=null && dakoku.getEnd() == null) {
                requestScope("errorMessage", "退勤の打刻もれがあります。修正してから勤務表を提出してください。");
            }
            if(dakoku.getOut() != null && dakoku.getIn() == null) {
                requestScope("errorMessage", "戻りの打刻もれがあります。修正してから勤務表を提出してください。");
            }
        }
        
        List<ShuseiShinsei> ssList = ssService.getShinseiChuList(user, calRange[0], calRange[1]);
        if(ssList.size() > 0) {
            requestScope("errorMessage", "申請中の処理があります。処理が終了してから勤務表を提出してください。");
        }
        
        if(loginUser.isAuthKintai()) {
            List<User> userList = userService.getAll(loginYago);
            requestScope("userList", userList);
        }
        
        requestScope("dayList", dayList);
        requestScope("map", map);
        requestScope("dateMap", dateMap);
        
        return forward("teishutsu.jsp");
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
