package agri.controller.hanbai.shukka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.User;
import agri.model.hanbai.Okurisaki;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.Const;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.UserService;
import agri.service.Util;
import agri.service.hanbai.OkurisakiService;
import agri.service.hanbai.ShukkaKeitaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class CreateController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    private ShukkaKeitaiService skService = new ShukkaKeitaiService();
    private UserService userService = new UserService();
    private OkurisakiService oService = new OkurisakiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<Torihikisaki> tList = tService.getAll(loginYago);
        List<Hinmoku> hList = hService.getAll(loginYago);
        
        StringBuffer sb = new StringBuffer();
        if(tList.size() == 0) {
            sb.append("【取引先】を登録してから出荷登録を行ってください。");   
        }
        if(hList.size() == 0) {
            sb.append("【品目】を登録してから出荷登録を行ってください。");
        }
        if(sb.length() > 0) {
            requestScope("errorStr", sb.toString());
            return forward("../");
        }
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
        
        requestScope("tList", tList);
        requestScope("hList", hList);
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        requestScope("taxConst", Const.TAX);
        requestScope("itemCount", 1);
        return forward("create.jsp");
    }
}
