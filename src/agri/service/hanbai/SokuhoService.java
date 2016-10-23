package agri.service.hanbai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import agri.model.Chokubai;
import agri.model.Hinmoku;
import agri.model.Yago;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaCount;
import agri.model.hanbai.ShukkaKingaku;
import agri.model.hanbai.UriageCount;
import agri.model.hanbai.UriageKingaku;
import agri.model.mail.OneNotice;
import agri.service.Util;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Key;

public class SokuhoService {
    
    private OneNoticeService onService = new OneNoticeService();
    private UriageKingakuService ukService = new UriageKingakuService();
    private UriageCountService ucService = new UriageCountService();
    private ShukkaService sService = new ShukkaService();
    private ShukkaKingakuService skService = new ShukkaKingakuService();
    private ShukkaCountService scService = new ShukkaCountService();

    public SokuhoMapDto get7daysMap(Yago yago, Date date) {
        
        HashMap<Key, SokuhoDto> map = new HashMap<Key, SokuhoDto>();
        Calendar startCal = Util.getCalendar();
        startCal.setTime(date);
        startCal.add(Calendar.DAY_OF_MONTH, -7);
        Util.resetTime(startCal);
       
        Calendar endCal = Util.getCalendar();
        endCal.setTime(date);
        Util.resetTime(endCal);

        // 売上数
        List<OneNotice> onList = onService.getBy7hours(yago, startCal, endCal);
        
        for(OneNotice on : onList) {
            List<UriageKingaku> ukList = ukService.getListByOneNotice(on);
            for(UriageKingaku uk : ukList) {
                SokuhoDto dto;
                Key cKey = uk.getChokubaiRef().getKey();
                if(map.containsKey(uk.getChokubaiRef().getKey())) {
                    dto = map.get(cKey);
                    UriageKingaku uk0 = dto.getUk();
                    uk0.setKingaku(uk0.getKingaku() + uk.getKingaku());
                } else {
                    dto = new SokuhoDto();
                    map.put(cKey, dto);
                    dto.setUk(uk);
                }
                List<UriageCount> ucList = ucService.getListByUriageKingaku(uk);
                for(UriageCount uc : ucList) {
                    Hinmoku hinmoku = uc.getHinmokuRef().getModel();
                    Key hKey = hinmoku.getKey();
                    if(dto.getUcMap().containsKey(hKey)) {
                        dto.getUcMap().put(hKey, new Integer(dto.getUcMap().get(hKey) + uc.getCount()));
                    } else {
                        dto.getUcMap().put(hKey, uc.getCount());
                    }
                }
            }
        }
        
        // 出荷数
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        endCal.add(Calendar.DAY_OF_MONTH, -1);
        
        List<Shukka> shukkaList = sService.getChokubai(yago, startCal.getTime(), endCal.getTime());
        
        for(Shukka shukka : shukkaList) {
            List<ShukkaKingaku> skList = skService.getListByShukka(shukka);
            for(ShukkaKingaku sk : skList) {
                SokuhoDto dto;
                Key cKey = sk.getChokubaiRef().getKey();
                if(map.containsKey(cKey)) {
                    dto = map.get(cKey);
                    if(dto.getSk() == null) {
                        dto.setSk(sk);
                    } else {
                        dto.getSk().setKingaku(dto.getSk().getKingaku() + sk.getKingaku());
                    }
                } else {
                    dto = new SokuhoDto();
                    map.put(cKey, dto);
                    dto.setSk(sk);
                }
                
                List<ShukkaCount> scList = scService.getListByShukkaKingaku(sk);
                for(ShukkaCount sc : scList) {
                    Hinmoku hinmoku = sc.getHinmokuRef().getModel();
                    Key hKey = hinmoku.getKey();
                    if(dto.getScMap().containsKey(hKey))     {
                        dto.getScMap().put(hKey, dto.getScMap().get(hKey) + sc.getCount());
                    } else {
                        dto.getScMap().put(hKey, sc.getCount());
                    }
                }
            }
        }
        
        SokuhoMapDto mapDto = new SokuhoMapDto(0, 0, map);
        return mapDto;
    }
    
    public SokuhoMapDto getSokuhoMap(Yago yago, Date date, int amountDay) {
        
        Calendar cal = Util.getCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, amountDay);
        
        HashMap<Key, SokuhoDto> map = new HashMap<Key, SokuhoDto>();
        
        List<OneNotice> onList = onService.getByNearLessDate(yago, cal.getTime());
        if(onList.size() == 0) return new SokuhoMapDto(0, 0, map);
        
        List<Hinmoku> hinmokuList = new ArrayList<Hinmoku>();
        List<Chokubai> chokubaiList = new ArrayList<Chokubai>();
        HashMap<Key, Integer> shukkaSum = new HashMap<Key, Integer>();
        HashMap<Key, Integer> uriageSum = new HashMap<Key, Integer>();
        
        int skSum = 0;
        int ukSum = 0;
        
        // 売上数
        OneNotice on = onList.get(0);
        cal = Util.getCalendar();
        cal.setTime(on.getDate());
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
        
        List<Shukka> shukkaList = sService.getChokubai(yago, startDate, endDate);
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
        SokuhoMapDto mapDto = new SokuhoMapDto(skSum, ukSum, map);
        return mapDto;
    }
    
    public class SokuhoMapDto {
        
        private int skSum; // 使ってない？？
        private int ukSum; // 使ってない？？
        private HashMap<Key, SokuhoDto> map;
        
        public SokuhoMapDto(int skSum, int ukSum, HashMap<Key, SokuhoDto> map) {
            this.skSum = skSum;
            this.ukSum = ukSum;
            this.map = map;
        }
        
        public int getSkSum() {
            return skSum;
        }
        
        public int getUkSum() {
            return ukSum;
        }
        
        public HashMap<Key, SokuhoDto> getMap() {
            return map;
        }
    }
}
