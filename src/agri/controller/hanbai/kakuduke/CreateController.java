package agri.controller.hanbai.kakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.service.Util;

public class CreateController extends BaseController {

    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<User> list = userService.getKakudukeTantoList(loginYago);
        requestScope("list", list);
        requestScope("kind", 1);
        
        Calendar today = Util.getCalendar();
        requestScope("year", today.get(Calendar.YEAR));
        requestScope("month", today.get(Calendar.MONTH) + 1);
        requestScope("day", today.get(Calendar.DAY_OF_MONTH));
        
        return forward("create.jsp");
    }
}
