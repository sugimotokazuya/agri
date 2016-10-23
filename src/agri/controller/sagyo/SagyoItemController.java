package agri.controller.sagyo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.service.Util;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SagyoService;

import com.google.appengine.api.datastore.Key;

public class SagyoItemController extends BaseController {

    private SagyoItemService siService = new SagyoItemService();
    private SagyoService sService = new SagyoService();
    
    @Override
    public boolean validateAuth() {
        Key userKey = asKey("userRef");
        if(userKey != null) {
            User user = userService.get(userKey);
            if(!user.getYagoRef().getModel().equals(loginYago)) return false;
        }
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        if(request.getAttribute("pdf") != null) {
            return forward("sagyoItemPdf");
        }
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        Integer yearI = asInteger("year");
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        requestScope("year", yearI);
        requestScope("siList", siService.getAll(loginYago));
        
        if(Util.isEmpty(asString("itemRef"))) return forward("sagyoItem.jsp");
        Key itemKey = asKey("itemRef");
        SagyoItem si;
        if(itemKey == null) {
            return forward("sagyoItem.jsp");
        } else {
            si = siService.get(itemKey);
        }
        
        Date[] range = Util.createOneYearRange(yearI);
        List<Sagyo> list = sService.getByDateRangeSagyoItem(loginYago, range[0], range[1], si);
        requestScope("list", list);
        
        Map<Sakuduke, Double> map = new HashMap<Sakuduke, Double>();
        for(Sagyo s : list) {
            Sakuduke sakuduke = s.getSakudukeRef().getModel();
            Double sum = s.getAmount();
            if(sum == null) sum = 0d;
            if(map.containsKey(sakuduke)) {
                sum += map.get(sakuduke);
            }
            map.put(sakuduke, sum);
        }
        requestScope("map", map);
        requestScope("si", si);
        
        return forward("sagyoItem.jsp");
    }
}
