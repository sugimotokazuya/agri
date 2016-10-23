package agri.controller.hanbai.seikyu;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.hanbai.Seikyu;
import agri.model.hanbai.SeikyuRec;
import agri.service.Util;
import agri.service.hanbai.SeikyuService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;

public class UpdateController extends BaseController {

    private SeikyuService service = new SeikyuService();
    private ShukkaService sService = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        Seikyu s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthSeikyuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Seikyu s = service.get(asKey("key"), asLong("version"));
        
        List<SeikyuRec> recList = service.getRecList(asKey("key"));
        Key[] keys = new Key[recList.size()];
        for(int i = 0; i < keys.length; ++i) {
            keys[i] = recList.get(i).getShukkaRef().getKey();
        }
        
        if (!validate()) {
            List<ShukkaDto> list = sService.getDtoList(keys);
            requestScope("list", list);
            requestScope("s", s);
            return forward("edit.jsp");
        }
        
        Calendar cal1 = Util.getCalendar();
        cal1.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));

        Calendar cal2 = Util.getCalendar();
        cal2.set(asInteger("limitYear"), asInteger("limitMonth") - 1, asInteger("limitDay"));

        s.setDate(cal1.getTime());
        s.setLimitDate(cal2.getTime());
        service.update(s);
        
        return redirect(basePath + "./");
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
