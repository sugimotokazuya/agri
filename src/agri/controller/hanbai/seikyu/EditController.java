package agri.controller.hanbai.seikyu;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Seikyu;
import agri.model.hanbai.SeikyuRec;
import agri.service.Util;
import agri.service.hanbai.SeikyuService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;

public class EditController extends BaseController {

    private ShukkaService sService = new ShukkaService();
    private SeikyuService service = new SeikyuService();
    
    @Override
    public boolean validateAuth() {
    
        Seikyu s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthSeikyuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Seikyu s = service.get(asKey("key"), asLong("version"));
        requestScope("s", s);
        
        List<SeikyuRec> recList = service.getRecList(asKey("key"));
        Key[] keys = new Key[recList.size()];
        for(int i = 0; i < keys.length; ++i) {
            keys[i] = recList.get(i).getShukkaRef().getKey();
        }
        
        List<ShukkaDto> list = sService.getDtoList(keys);
        requestScope("list", list);
        
        Calendar cal = Util.getCalendar();
        cal.setTime(s.getDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        cal.setTime(s.getLimitDate());
        requestScope("limitYear", cal.get(Calendar.YEAR));
        requestScope("limitMonth", cal.get(Calendar.MONTH) + 1);
        requestScope("limitDay", cal.get(Calendar.DAY_OF_MONTH));
        
        return forward("edit.jsp");
    }
    
}
