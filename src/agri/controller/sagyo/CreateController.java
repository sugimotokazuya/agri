package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.GazoService;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.KeyFactory;

public class CreateController extends BaseController {

    SakudukeService service = new SakudukeService();
    SakudukeMeta meta = SakudukeMeta.get();
    SagyoItemService sagyoItemService = new SagyoItemService();
    UserService userService = new UserService();
    private GazoService gazoService = new GazoService();
    
    @Override
    public boolean validateAuth() {
        
        SagyoItem sagyoItem = sagyoItemService.get(asKey("sagyoItem"));
        if(!sagyoItem.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        requestScope("userList", userService.getAll(loginYago));
        
        SagyoItem sagyoItem = sagyoItemService.get(asKey("sagyoItem"));
        requestScope("sagyoItem", sagyoItem);
        requestScope("sagyoItemKey", sagyoItem.getKey());
        
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("sakuduke", sakuduke);
        requestScope("sakudukeKey", sakuduke.getKey());
        
        requestScope("user", 
            KeyFactory.keyToString(sakuduke.getTantoRef().getKey()));
        
        List<Gazo> gazoList = gazoService.getByUser(loginUser);
        requestScope("gazoList", gazoList);
        
        requestScope("user1", KeyFactory.keyToString(loginUser.getKey()));
        
        return forward("create.jsp");
    }
}
