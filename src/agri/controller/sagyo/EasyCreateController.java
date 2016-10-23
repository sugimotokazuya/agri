package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SakudukeService;

public class EasyCreateController extends BaseController {

    SakudukeService service = new SakudukeService();
    SagyoItemService sagyoItemService = new SagyoItemService();
    UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        
        SagyoItem sagyoItem = sagyoItemService.get(asKey("sagyoItemKey"));
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
        
        SagyoItem sagyoItem = sagyoItemService.get(asKey("sagyoItemKey"));
        requestScope("sagyoItem", sagyoItem);
        requestScope("sagyoItemKey", sagyoItem.getKey());
        
        List<Sakuduke> list = service.getEasyInputList(loginYago);
        requestScope("list", list);
        
        List<User> userList = userService.getAll(loginYago);
        requestScope("userList", userList);
        
        return forward("easyCreate.jsp");
    }
}
