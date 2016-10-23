package agri.controller.sagyo.sakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Hatake;
import agri.model.User;
import agri.service.HatakeService;
import agri.service.UserService;
import agri.service.Util;

public class CreateController extends BaseController {

    private HatakeService hatakeService = new HatakeService();
    private UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        List<Hatake> hatakeList = hatakeService.getAll(loginYago);
        
        StringBuffer sb = new StringBuffer();
        if(hatakeList.size() == 0) {
            sb.append("【畑】を登録してから作付登録を行ってください。");
        }
        if(sb.length() > 0) {
            requestScope("errorStr", sb.toString());
            return forward("./");
        }
        
        requestScope("hatakeList", hatakeList);
        
        List<User> userList = userService.getAll(loginYago);
        requestScope("userList", userList);
        
        return forward("create.jsp");
    }
}
