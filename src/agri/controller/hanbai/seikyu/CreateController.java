package agri.controller.hanbai.seikyu;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Shukka;
import agri.service.Util;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CreateController extends BaseController {

    private ShukkaService sService = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        
        String[] values = request.getParameterValues("sKey");
        Key[] keys = new Key[values.length];
        for(int i = 0; i < keys.length; ++i) {
            String key = values[i].substring(4);
            keys[i] = KeyFactory.stringToKey(key);
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
            String key = values[i].substring(4);
            keys[i] = KeyFactory.stringToKey(key);
        }
        
        List<ShukkaDto> list = sService.getDtoList(keys);
        requestScope("list", list);
        requestScope("torihikisaki", list.get(0).getShukka().getTorihikisakiRef().getModel());
        requestScope("seikyuKingaku", sService.getSumGokei(list));
        
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        requestScope("limitYear", cal.get(Calendar.YEAR));
        requestScope("limitMonth", cal.get(Calendar.MONTH) + 1);
        requestScope("limitDay", cal.get(Calendar.DAY_OF_MONTH));
        
        return forward("create.jsp");
    }
    
}
