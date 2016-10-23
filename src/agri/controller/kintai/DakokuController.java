package agri.controller.kintai;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Dakoku;
import agri.service.UserService;
import agri.service.Util;
import agri.service.kintai.DakokuService;

import com.google.appengine.api.datastore.Key;

public class DakokuController extends BaseController {

    private UserService userService = new UserService();
    private DakokuService dakokuService = new DakokuService();

    @Override
    public Navigation run() throws Exception {
        
        Calendar today = Util.getCalendar();
        Util.resetTime(today);
        Calendar now = Util.getCalendar();
        Util.resetSecond(now);
        
        if(asString("key") != null) {
            // 打刻
            Key userKey = asKey("key");
            Dakoku dakoku = dakokuService.get(userKey, today.getTime());
            int status = asInteger("status");
            switch(status) {
            case 1:
                dakoku = new Dakoku();
                dakoku.getUserRef().setKey(userKey);
                dakoku.setDate(today.getTime());
                dakoku.setStart(now.getTime());
                dakokuService.insert(dakoku);
                break;
            case 2:
                dakoku.setEnd(now.getTime());
                dakokuService.update(dakoku);
                break;
            case 4:
                dakoku.setOut(now.getTime());
                dakokuService.update(dakoku);
                break;
            case 5:
                dakoku.setIn(now.getTime());
                dakokuService.update(dakoku);
                break;
            case 6:
                dakoku.setEnd(now.getTime());
                dakokuService.update(dakoku);
                break;
            }
            
            return null;
        }

        List<User> list = userService.getUseDakokuList(loginYago);
        Map<Key, Integer> map = new HashMap<Key, Integer>();
        
        // Status
        // 0:出勤前, 1:出勤, 2:退勤済み, 4:外出中, 5:戻り, 6:戻りからの退勤
        for(User user : list) {
            Dakoku dakoku = dakokuService.get(user.getKey(), today.getTime());
            int status = 0;
            if(dakoku == null) {
                status = 0;
            } else if(dakoku.getEnd() == null) {
                if(dakoku.getOut() == null) {
                    status = 1;
                } else if(dakoku.getIn() == null) {
                    status = 4;
                } else {
                    status = 5;
                }
            } else if(dakoku.getEnd() != null) {
                if(dakoku.getIn() == null) {
                    status = 2;
                } else {
                    status = 6;
                }
            }
            map.put(user.getKey(), status);
        }
        
        requestScope("userList", list);
        requestScope("size", list.size());
        requestScope("map", map);
        requestScope("today", today.getTime());
        
        return forward("dakoku.jsp");
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.equals(loginYago.getKintaiRef().getModel())
                || loginUser.isAuthKintai();
    }
}
