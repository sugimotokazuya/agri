package agri.controller.kintai.kinmuhyo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.Dakoku;
import agri.model.kintai.Kinmuhyo;
import agri.model.kintai.Teiji;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.KinmuhyoService;
import agri.service.kintai.TeijiService;

import com.google.appengine.api.datastore.Key;

public class ViewController extends BaseController {

    private DakokuService dakokuService = new DakokuService();
    private TeijiService teijiService = new TeijiService();
    private KinmuhyoService kService = new KinmuhyoService();
    
    @Override
    public boolean validateAuth() {
        Key key = asKey("key");
        long version = asLong("version");
        
        Kinmuhyo k = kService.get(key, version);
        if(loginUser.isAuthKintai() && !loginYago.equals(k.getYagoRef().getModel())) {
            return false;
        } else if(!loginUser.isAuthKintai() && !loginUser.equals(k.getUserRef().getModel())) {
            return false;
        }
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return redirect("/kintai/kinmuhyo");
        }
        
        Key key = asKey("key");
        long version = asLong("version");
        
        Kinmuhyo k = kService.get(key, version);
        requestScope("kinmuhyo", k);
        
        Calendar cal = Util.getCalendar();
        cal.setTime(k.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        
        Calendar kCal = Util.getCalendar();
        kCal.setTime(k.getDate());
        kCal.add(Calendar.MONTH, -1);
        Kinmuhyo zenK = kService.get(k.getUserRef().getModel(), kCal.getTime());
        if(zenK != null) {
            if(month == 3) {
                k.setYasumiZengetsuZan(0);
            } else {
                k.setYasumiZengetsuZan(zenK.getYasumiZan());
            }
        }
        
        
        Date[] calRange = Util.createOneMonthRange(year, month);
        
        List<Dakoku> list = dakokuService.getList(
            k.getUserRef().getModel(), calRange[0], calRange[1]);
        
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
        }
        
        // 休みの合計時間の計算
        int yasumiMinute = 0;
        int kinmuMinute = 0;
        int zangyoMinutes = 0;
        for(Integer day : dayList) {
            Teiji teiji = teijiMap.get(day);
            Dakoku dakoku = map.get(day);
            int teijiMinute = Util.getDiffMinute(teiji.getStartTime(), teiji.getEndTime()) - 60;
            if(dakoku == null) {
                yasumiMinute += teijiMinute;
            } else {
                int workMinute = Util.getDiffMinute(
                    dakoku.getStartInt() < teiji.getStartTimeInt() ? teiji.getStartTime() : dakoku.getStart()
                    , dakoku.getEndInt() > teiji.getEndTimeInt() ? teiji.getEndTime() : dakoku.getEnd());
                if(dakoku.getOut() != null) {
                    workMinute -= Util.getDiffMinute(dakoku.getOut(), dakoku.getIn());
                }
                if(dakoku.isOhiru()) workMinute -= 60;
                kinmuMinute += workMinute;
                if(dakoku.isYasumi()) {
                    yasumiMinute += teijiMinute - workMinute;
                    System.out.println(yasumiMinute + ":" + teijiMinute + ":" + workMinute);
                }
                if(dakoku.isZangyo()) {
                    Calendar zanStart = Calendar.getInstance();
                    zanStart.setTime(teiji.getEndTime());
                    int startMinute = zanStart.get(Calendar.HOUR_OF_DAY) * 60 + zanStart.get(Calendar.MINUTE); 
                    Calendar zanEnd = Calendar.getInstance();
                    zanEnd.setTime(dakoku.getEnd());
                    int endMinute = zanEnd.get(Calendar.HOUR_OF_DAY) * 60 + zanEnd.get(Calendar.MINUTE);
                    int jikan = endMinute - startMinute;
                    zangyoMinutes += jikan;
                    kinmuMinute += jikan;
                }
            }
        }
        
        requestScope("dayList", dayList);
        requestScope("map", map);
        requestScope("dateMap", dateMap);
        int yasumiHour = yasumiMinute / 60;
        if(yasumiMinute % 60 > 0) yasumiHour += 1;
        requestScope("yasumiHour", yasumiHour);
        requestScope("yasumiZengetsuZan", k.getYasumiZengetsuZan());
        requestScope("yasumiPlus", k.getYasumiPlus());
        requestScope("yasumiZan", k.getYasumiZan());
        requestScope("kinmuDays", list.size());
        int kinmuHours = kinmuMinute / 60;
        requestScope("kinmuHours", kinmuHours);
        requestScope("zangyoMinutes", zangyoMinutes);
        
        return forward("view.jsp");
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
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.maxlength(50));
        v.add("version", v.longType());
        
        return v.validate();
    }
}
