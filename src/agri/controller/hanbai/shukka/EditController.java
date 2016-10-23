package agri.controller.hanbai.shukka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.Okurisaki;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaKeitai;
import agri.model.hanbai.ShukkaRec;
import agri.service.Const;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;
import agri.service.hanbai.OkurisakiService;
import agri.service.hanbai.ShukkaKeitaiService;
import agri.service.hanbai.ShukkaRecService;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.KeyFactory;

public class EditController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    private ShukkaService service = new ShukkaService();
    private ShukkaRecService recService = new ShukkaRecService();
    private ShukkaKeitaiService skService = new ShukkaKeitaiService();
    private KakudukeService kService = new KakudukeService();
    private OkurisakiService oService = new OkurisakiService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        // TODO 取引先が直売の場合は同じ品目でShukkaRecに登録させないようにしないといけない
        
        requestScope("tList", tService.getAll(loginYago));
        requestScope("hList", hService.getAll(loginYago));
        requestScope("taxConst", Const.TAX);
        Shukka s = service.get(asKey("key"), asLong("version"));
        List<ShukkaRec> recList = recService.getList(s);
        String tKey = KeyFactory.keyToString(s.getTorihikisakiRef().getKey());
        requestScope("torihikisaki",tKey);
        Calendar cal = Util.getCalendar();
        cal.setTime(s.getDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        BeanUtil.copy(s, request);
        
        @SuppressWarnings("rawtypes")
        List[] skListArray =  new ArrayList[recList.size()];
        for(int i = 0; i < recList.size(); ++i) {
            int j = i + 1;
            ShukkaRec rec = recList.get(i);
            requestScope("hinmoku" + j,KeyFactory.keyToString(rec.getHinmokuRef().getKey()));
            requestScope("tanka" + j, rec.getTanka());
            requestScope("suryo" + j, rec.getSuryo());
            requestScope("biko" + j, rec.getBiko());
            requestScope("shukkaKeitai" + j, KeyFactory.keyToString(rec.getShukkaKeitaiRef().getKey()));
            List<ShukkaKeitai> skList = skService.get(s.getTorihikisakiRef().getModel(), rec.getHinmokuRef().getModel());
            skListArray[i] = skList;
            Kakuduke k = kService.getByShukkaRec(rec);
            if(k != null) {
                requestScope("kakuduke" + j, -k.getPlus());
            }
        }
        requestScope("skListArray", skListArray);
        
        if(s.getTax() == 0) requestScope("taxKomi", true);
        requestScope("itemCount", recList.size());
        
        Map<String, List<ShukkaKeitai>> skMap = new HashMap<String, List<ShukkaKeitai>>();
        List<ShukkaKeitai> skList = skService.getAll(loginYago);
        for(ShukkaKeitai sk : skList) {
            String key = KeyFactory.keyToString(sk.getTorihikisakiRef().getKey()) + ":"
                    + KeyFactory.keyToString(sk.getHinmokuRef().getKey());
            if(!skMap.containsKey(key)) {
                skMap.put(key, new ArrayList<ShukkaKeitai>());
            }
            List<ShukkaKeitai> list = skMap.get(key);
            list.add(sk);
        }
        requestScope("skMap", skMap);
        
        // 送り先を屋号で取得して取引先毎にMapに格納する
        Map<String, List<Okurisaki>> oMap = new HashMap<String, List<Okurisaki>>();
        List<Okurisaki> oList = oService.getByYago(loginYago);
        for(Okurisaki o : oList) {
            String key = KeyFactory.keyToString(o.getTorihikisakiRef().getKey());
            if(!oMap.containsKey(key)) {
                oMap.put(key, new ArrayList<Okurisaki>());
            }
            List<Okurisaki> list = oMap.get(key);
            list.add(o);
        }
        requestScope("oMap", oMap);
        
        List<User> kakudukeTantoList = userService.getKakudukeTantoList(loginYago);
        requestScope("kakudukeTantoList", kakudukeTantoList);
        
        Kakuduke k = kService.getByShukka(s);
        if(k != null) {
            requestScope("kakudukeTantoRef", KeyFactory.keyToString(k.getUserRef().getKey()));
            requestScope("kakuduke", - k.getPlus());
        }
        
        return forward("edit.jsp");
    }
}
