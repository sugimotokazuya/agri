package agri.controller.sagyo.sakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.Hatake;
import agri.model.User;
import agri.model.sagyo.Sakuduke;
import agri.service.HatakeService;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.KeyFactory;


public class EditController extends BaseController {

    private SakudukeMeta meta = SakudukeMeta.get();
    private SakudukeService service = new SakudukeService();
    private HatakeService hatakeService = new HatakeService();
    private UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSakudukeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(sakuduke, request);
        
        Calendar cal = Util.getCalendar();
        cal.setTime(sakuduke.getStartDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", (cal.get(Calendar.MONTH) + 1));
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        List<Hatake> hatakeList = hatakeService.getAll(loginYago);
        requestScope("hatakeList", hatakeList);
        requestScope("hatake", KeyFactory.keyToString(
            sakuduke.getHatakeRef().getModel().getKey()));
        
        List<User> userList = userService.getAll(loginYago);
        requestScope("userList", userList);
        requestScope("tanto", KeyFactory.keyToString(
            sakuduke.getTantoRef().getModel().getKey()));
        requestScope("adminUser", KeyFactory.keyToString(
            sakuduke.getAdminUserRef().getModel().getKey()));
        
        return forward("edit.jsp");
    }
}
