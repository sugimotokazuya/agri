package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.sagyo.SagyoUser;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.SagyoUserService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserController extends BaseController {

    private SagyoUserService sagyoUserService = new SagyoUserService();
    private UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        Key userKey = asKey("userRef");
        if(userKey != null) {
            User user = userService.get(userKey);
            if(!user.getYagoRef().getModel().equals(loginYago)) return false;
        }
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Key userKey = asKey("userRef");
        User user;
        if(userKey == null) {
            user = loginUser;
            requestScope("userRef", KeyFactory.keyToString(user.getKey()));
        } else {
            user = userService.get(userKey);
        }
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        Integer yearI = asInteger("year");
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        requestScope("year", yearI);
        
        List<SagyoUser> list = sagyoUserService.getListByUser(user, yearI);
        requestScope("list", list);
        requestScope("user", user);
        requestScope("userList", userService.getAll(loginYago));
        
        return forward("user.jsp");
    }
}
