package agri.controller.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.mail.OneNoticeMeta;
import agri.model.Chokubai;
import agri.model.Hinmoku;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaCount;
import agri.model.hanbai.ShukkaKingaku;
import agri.model.hanbai.UriageCount;
import agri.model.hanbai.UriageKingaku;
import agri.model.mail.OneNotice;
import agri.service.ChokubaiService;
import agri.service.HinmokuService;
import agri.service.Util;
import agri.service.hanbai.ShukkaCountService;
import agri.service.hanbai.ShukkaKingakuService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.UriageCountService;
import agri.service.hanbai.UriageKingakuService;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Key;

public class SokuhoController extends BaseController {

    private OneNoticeService onService = new OneNoticeService();
    private UriageKingakuService ukService = new UriageKingakuService();
    private UriageCountService ucService = new UriageCountService();
    private ShukkaService sService = new ShukkaService();
    private ShukkaKingakuService skService = new ShukkaKingakuService();
    private ShukkaCountService scService = new ShukkaCountService();
    private ChokubaiService cService = new ChokubaiService();
    private HinmokuService hService = new HinmokuService();
    private OneNoticeMeta onMeta = OneNoticeMeta.get(); 
    
    @Override
    public boolean validateAuth() {
        OneNotice on = onService.get(asKey(onMeta.key), asLong(onMeta.version));
        if(!on.getYagoRef().getModel().equals(loginYago)) {
            return false;
        }
        return loginUser.isAuthMailView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        boolean chokubaiEdit = asBoolean("chokubaiEdit") == null ? false : asBoolean("chokubaiEdit");
        
        List<Hinmoku> hinmokuList = new ArrayList<Hinmoku>();
        List<Chokubai> chokubaiList = new ArrayList<Chokubai>();
        HashMap<Key, Integer> shukkaSum = new HashMap<Key, Integer>();
        HashMap<Key, Integer> uriageSum = new HashMap<Key, Integer>();
        HashMap<Key, SokuhoDto> map = new HashMap<Key, SokuhoDto>();
        int skSum = 0;
        int ukSum = 0;
        
        // 売上数
        Key key = asKey(onMeta.key);
        long version = asLong(onMeta.version);
        OneNotice on = onService.get(key, version);
        if(on == null) return forward("noSokuho.jsp");
        requestScope("on", on);
        cal.setTime(on.getDate());
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        List<UriageKingaku> ukList = ukService.getListByOneNotice(on);
        Iterator<UriageKingaku> ite = ukList.iterator();
        while(ite.hasNext()) {
            UriageKingaku uk = ite.next();
            ukSum += uk.getKingaku();
            SokuhoDto dto = new SokuhoDto();
            map.put(uk.getChokubaiRef().getKey(), dto);
            chokubaiList.add(uk.getChokubaiRef().getModel());
            dto.setUk(uk);
            List<UriageCount> ucList = ucService.getListByUriageKingaku(uk);
            Iterator<UriageCount> ucIte = ucList.iterator();
            while(ucIte.hasNext()) {
                UriageCount uc = ucIte.next();
                Hinmoku hinmoku = uc.getHinmokuRef().getModel();
                Key hKey = hinmoku.getKey();
                if(!hinmokuList.contains(hinmoku)) {
                    hinmokuList.add(hinmoku);
                    uriageSum.put(hKey, uc.getCount());
                } else {
                    uriageSum.put(hKey, uriageSum.get(hKey) + uc.getCount());
                }
                dto.getUcMap().put(hKey, uc.getCount());
            }
        }
        
        //出荷数
        if(cal.get(Calendar.HOUR_OF_DAY) > 8) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -2);
        }
        Util.resetTime(cal);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();
        
        List<Shukka> shukkaList = sService.getChokubai(loginYago, startDate, endDate);
        Iterator<Shukka> shukkaIte = shukkaList.iterator();
        while(shukkaIte.hasNext()) {
            Shukka shukka = shukkaIte.next();
            List<ShukkaKingaku> skList = skService.getListByShukka(shukka);
            Iterator<ShukkaKingaku> skIte = skList.iterator();
            while(skIte.hasNext()) {
                ShukkaKingaku sk = skIte.next();
                skSum += sk.getKingaku();
                SokuhoDto dto;
                if(map.containsKey(sk.getChokubaiRef().getKey())) {
                    dto = map.get(sk.getChokubaiRef().getKey());
                } else {
                    dto = new SokuhoDto();
                    map.put(sk.getChokubaiRef().getKey(), dto);
                    chokubaiList.add(sk.getChokubaiRef().getModel());
                }
                dto.setSk(sk);
                List<ShukkaCount> scList = scService.getListByShukkaKingaku(sk);
                Iterator<ShukkaCount> scIte = scList.iterator();
                while(scIte.hasNext()) {
                    ShukkaCount sc = scIte.next();
                    Hinmoku hinmoku = sc.getHinmokuRef().getModel();
                    Key hKey = hinmoku.getKey();
                    if(!shukkaSum.containsKey(hKey)) {
                        shukkaSum.put(hKey, 0);
                    }
                    if(!hinmokuList.contains(hinmoku)) {
                        hinmokuList.add(hinmoku);
                        shukkaSum.put(hKey, sc.getCount());
                    } else {
                        shukkaSum.put(hKey, shukkaSum.get(hKey) + sc.getCount());
                    }
                    dto.getScMap().put(hinmoku.getKey(), sc.getCount());
                }
            }
        }
        
        requestScope("map", map);
        hService.sort(hinmokuList);
        requestScope("hinmokuList", hinmokuList);
        cService.sort(chokubaiList);
        requestScope("chokubaiList", chokubaiList);
        requestScope("ukSumMap", uriageSum);
        requestScope("skSumMap", shukkaSum);
        requestScope("ukSum", ukSum);
        requestScope("skSum", skSum);
        
        if(chokubaiEdit) {
            
            return forward("sokuhoIFrame.jsp");
        } else {
            return forward("sokuho.jsp");
        }
    }
    
    public class SokuhoDto {
        
        private UriageKingaku uk;
        private ShukkaKingaku sk;
        
        // Keyは品目
        private HashMap<Key, Integer> ucMap = new HashMap<Key, Integer>();
        private HashMap<Key, Integer> scMap = new HashMap<Key, Integer>();
        
        public UriageKingaku getUk() {
            return uk;
        }
        public void setUk(UriageKingaku uk) {
            this.uk = uk;
        }
        public HashMap<Key, Integer> getUcMap() {
            return ucMap;
        }
        public ShukkaKingaku getSk() {
            return sk;
        }
        public void setSk(ShukkaKingaku sk) {
            this.sk = sk;
        }
        public HashMap<Key, Integer> getScMap() {
            return scMap;
        }
    }
}
