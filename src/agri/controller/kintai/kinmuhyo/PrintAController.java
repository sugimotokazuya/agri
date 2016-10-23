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
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.KinmuhyoService;

import com.google.appengine.api.datastore.Key;

public class PrintAController extends BaseController {

    private DakokuService dakokuService = new DakokuService();
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
        
        requestScope("yasumiHour", k.getYasumiHour());
        requestScope("yasumiZengetsuZan", k.getYasumiZengetsuZan());
        requestScope("yasumiPlus", k.getYasumiPlus());
        requestScope("yasumiZan", k.getYasumiZan());
        
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
        Date[] calRange = Util.createOneMonthRange(year, month);
        
        List<Dakoku> list = dakokuService.getList(
            k.getUserRef().getModel(), calRange[0], calRange[1]);
        
        Calendar start = Util.getCalendar();
        start.setTime(calRange[0]);
        Calendar end = Util.getCalendar();
        end.setTime(calRange[1]);
        
        List<Integer> dayList = new ArrayList<Integer>();
        Map<Integer, Dakoku> map = new HashMap<Integer, Dakoku>();
        Map<Integer, Integer> kinmuJikanMap = new HashMap<Integer, Integer>();
        Map<Integer, Date> dateMap = new HashMap<Integer, Date>();
               
        while(start.getTimeInMillis() < end.getTimeInMillis()) {
            dayList.add(start.get(Calendar.DAY_OF_MONTH));
            Integer day = new Integer(start.get(Calendar.DAY_OF_MONTH));
            dateMap.put(day, start.getTime());
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        int sumKinmuJikan = 0;
        int kugiri = 10;
        Iterator<Dakoku> ite = list.iterator();
        while(ite.hasNext()) {
            Dakoku dakoku = ite.next();
            cal = Util.getCalendar();
            cal.setTime(dakoku.getDate());
            Integer day = new Integer(cal.get(Calendar.DAY_OF_MONTH));
            map.put(day, dakoku);
            Calendar calStart = Util.getCalendar();
            calStart.setTime(dakoku.getStart());
            if(calStart.get(Calendar.MINUTE) % kugiri > 0) {
                calStart.add(Calendar.MINUTE, kugiri - calStart.get(Calendar.MINUTE) % kugiri);
                dakoku.setStart(calStart.getTime());
            }
            Calendar calOut = Util.getCalendar();
            if(dakoku.getOut() != null) {
                calOut.setTime(dakoku.getOut());
                if(calOut.get(Calendar.MINUTE) % kugiri > 0) {
                    calOut.add(Calendar.MINUTE, - calOut.get(Calendar.MINUTE) % kugiri);
                    dakoku.setOut(calOut.getTime());
                }
            }
            Calendar calIn = Util.getCalendar();
            if(dakoku.getIn() != null) {
                calIn.setTime(dakoku.getIn());
                if(calIn.get(Calendar.MINUTE) % kugiri > 0) {
                    calIn.add(Calendar.MINUTE, kugiri - calIn.get(Calendar.MINUTE) % kugiri);
                    dakoku.setIn(calIn.getTime());
                }
            }
            Calendar calEnd = Util.getCalendar();
            calEnd.setTime(dakoku.getEnd());
            if(calEnd.get(Calendar.MINUTE) % kugiri > 0) {
                calEnd.add(Calendar.MINUTE, - calEnd.get(Calendar.MINUTE) % kugiri);
                dakoku.setEnd(calEnd.getTime());
            }
            
            // 勤務時間計算
            int jikan = (int) ((calEnd.getTimeInMillis() - calStart.getTimeInMillis()) / 1000 / 60);
            if(dakoku.isOhiru()) jikan -= 60;
            if(dakoku.getOut() != null) {
                int outJikan = (int) ((calIn.getTimeInMillis() - calOut.getTimeInMillis()) / 1000 / 60);
                jikan -= outJikan;
            }
            sumKinmuJikan += jikan;
            kinmuJikanMap.put(day, jikan);
        }
        
        requestScope("dayList", dayList);
        requestScope("map", map);
        requestScope("dateMap", dateMap);
        requestScope("kinmuJikanMap", kinmuJikanMap);
        
        requestScope("sumKinmuJikan", sumKinmuJikan);
        requestScope("sumJikanH", sumKinmuJikan / 60);
        requestScope("sumJikanM", sumKinmuJikan % 60);
        return forward("printA.jsp");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.maxlength(50));
        v.add("version", v.longType());
        
        return v.validate();
    }
}
