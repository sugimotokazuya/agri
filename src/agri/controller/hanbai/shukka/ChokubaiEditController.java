package agri.controller.hanbai.shukka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Chokubai;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaCount;
import agri.model.hanbai.ShukkaRec;
import agri.model.mail.OneNotice;
import agri.service.ChokubaiService;
import agri.service.Util;
import agri.service.hanbai.ShukkaCountService;
import agri.service.hanbai.ShukkaRecService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.SokuhoService;
import agri.service.hanbai.SokuhoService.SokuhoMapDto;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ChokubaiEditController extends BaseController {

    private ShukkaService sService = new ShukkaService();
    private ChokubaiService cService = new ChokubaiService();
    private ShukkaRecService recService = new ShukkaRecService();
    private ShukkaCountService scService = new ShukkaCountService();
    private OneNoticeService onService = new OneNoticeService();
    private SokuhoService sokuhoService = new SokuhoService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = sService.get(asKey("key"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        // TODO  取引先が直売の場合は同じ品目でShukkaRecに登録させないようにしないといけない
        
        Key shukkaKey = asKey("key");
        Shukka shukka = sService.get(shukkaKey);
        requestScope("shukka", shukka);
        
        List<Chokubai> chokubaiList = 
            cService.getListByTorihikisaki(shukka.getTorihikisakiRef().getModel());
        requestScope("chokubaiList", chokubaiList);
        requestScope("shopSize", chokubaiList.size());
        
        List<ShukkaRec> recList = recService.getList(shukka);
        requestScope("recList", recList);
        requestScope("recListSize", recList.size());
        
        Map<Key, ShukkaRec> recMap = new HashMap<Key, ShukkaRec>();
        Iterator<ShukkaRec> recIte = recList.iterator();
        while(recIte.hasNext()) {
            ShukkaRec rec = recIte.next();
            recMap.put(rec.getHinmokuRef().getKey(), rec);
        }
        
        List<ShukkaCount> list = scService.getListByShukka(shukka);
        Iterator<ShukkaCount> ite = list.iterator();
        while(ite.hasNext()) {
            ShukkaCount sc = ite.next();
            ShukkaRec rec = recMap.get(sc.getHinmokuRef().getKey());
            if(rec == null) {
                scService.delete(sc.getKey());
                continue;
            }
            String nameStr = KeyFactory.keyToString(rec.getKey()) 
                + "_" + KeyFactory.keyToString(
                sc.getShukkaKingakuRef().getModel().getChokubaiRef().getKey());
            requestScope(nameStr, sc.getCount());
        }
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.setTime(shukka.getDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        List<OneNotice> onList = 
                onService.getByNearLessDate(loginYago, cal.getTime());
        List<OneNotice> linkList = new ArrayList<OneNotice>();
        for(int i = 0; i < onList.size(); ++i) {
            if(i == 6) break;
            linkList.add(onList.get(i));
        }
        requestScope("linkList", linkList);
        
        requestScope("edit", true);
        
        cal = Util.getCalendar();
        cal.setTime(shukka.getDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        
        SokuhoMapDto dto1 = sokuhoService.getSokuhoMap(loginYago, cal.getTime(), 1);
        requestScope("dto1", dto1);
        
        cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.setTime(shukka.getDate());
        cal.set(Calendar.HOUR_OF_DAY, 7);
        
        SokuhoMapDto dto2 = sokuhoService.getSokuhoMap(loginYago, cal.getTime(), 0);
        requestScope("dto2", dto2);
        
        
        SokuhoMapDto dto7 = sokuhoService.get7daysMap(loginYago, shukka.getDate());
        requestScope("dto7", dto7);
        
        return forward("chokubai.jsp");
    }
}
