package agri.controller.hanbai.seikyu;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.model.hanbai.Shukka;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.SeikyuService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InsertController extends BaseController {

    private SeikyuService service = new SeikyuService();
    private ShukkaService sService = new ShukkaService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        Torihikisaki t = tService.get(asKey("torihikisaki"));
        if(!loginYago.equals(t.getYagoRef().getModel())) return false;
        String[] values = request.getParameterValues("sKey");
        Key[] keys = new Key[values.length];
        for(int i = 0; i < keys.length; ++i) {
            keys[i] = KeyFactory.stringToKey(values[i]);
        }
        for(Key key : keys) {
            Shukka s = sService.get(key);
            if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        }
        return loginUser.isAuthSeikyuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        String[] values = request.getParameterValues("sKey");
        Key[] keys = new Key[values.length];
        for(int i = 0; i < keys.length; ++i) {
            keys[i] = KeyFactory.stringToKey(values[i]);
        }
        
        if (!validate()) {
            List<ShukkaDto> list = sService.getDtoList(keys);
            requestScope("list", list);
            requestScope("torihikisaki", list.get(0).getShukka().getTorihikisakiRef().getModel());
            requestScope("seikyuKingaku", sService.getSumGokei(list));
            return forward("create.jsp");
        }
        
        Calendar cal1 = Util.getCalendar();
        cal1.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));

        Calendar cal2 = Util.getCalendar();
        cal2.set(asInteger("limitYear"), asInteger("limitMonth") - 1, asInteger("limitDay"));
        
        Torihikisaki t = tService.get(asKey("torihikisaki"));

        service.insert(loginYago, cal1.getTime(), cal2.getTime(), t, keys);
        
        return redirect(basePath + "../");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        
        v.add("limitYear", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("limitMonth", v.required(), v.longRange(1, 12));
        v.add("limitDay", v.required(), v.longRange(1, 31));
        
        return v.validate();
    }
}
