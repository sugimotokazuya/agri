package agri.controller.hanbai.shukka.okurijo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.model.hanbai.Okurisaki;
import agri.model.hanbai.Shukka;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.OkurisakiService;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.KeyFactory;

public class CreateController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private OkurisakiService oService = new OkurisakiService();
    private ShukkaService sService = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("../../");
        }
        
        int recordCount  = 15;
        
        List<Torihikisaki> tList = tService.getAll(loginYago);
        
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
        requestScope("tList", tList);
        requestScope("recordCount", recordCount);
        
        // 指定日の出荷データを初期値としてセットする
        Date[] range = Util.createOneDayRange(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        List<Shukka> list = sService.getList(loginYago, range[0], range[1]);
        @SuppressWarnings("rawtypes")
        List[] oListArray =  new ArrayList[recordCount];
        int i = 0;
        for(Shukka s : list) {
            ++i;
            String okurisakiName = Util.isEmpty(s.getOkurisaki()) ? s.getTorihikisakiName() : s.getOkurisaki();
            requestScope("kuchisu" + i, s.getKuchisu());
            requestScope("torihikisaki" + i, KeyFactory.keyToString(s.getTorihikisakiRef().getKey()));
            Okurisaki o = oService.getByName(loginYago, okurisakiName);
            if(o != null) {
                requestScope("okurisaki" + i, KeyFactory.keyToString(o.getKey()));
                requestScope("haitatsubi" + i, o.getHaitatsubi());
                requestScope("kiboujikan" + i, o.getKiboujikan());
            } else {
                requestScope("haitatsubi" + i, s.getTorihikisakiRef().getModel().getHaitatsubi());
                requestScope("kiboujikan" + i, s.getTorihikisakiRef().getModel().getKiboujikan());
            }
            
            List<Okurisaki> tempList = oService.getByTorihikisaki(s.getTorihikisakiRef().getModel());
            oListArray[i - 1] = tempList;
        }
        requestScope("oListArray", oListArray);
        return forward("create.jsp");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR), cal.get(Calendar.YEAR), "今年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        return v.validate();
    }
}
