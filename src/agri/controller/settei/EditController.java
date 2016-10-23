package agri.controller.settei;

import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.model.User;
import agri.service.UserService;

import com.google.appengine.api.datastore.KeyFactory;

public class EditController extends BaseController {

    private UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        return loginYago.getOwnerRef().getModel().equals(loginUser);
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<User> userList = userService.getAll(loginYago);
        requestScope("userList", userList);
        BeanUtil.copy(loginYago, request);
        if(loginYago.getKintaiRef().getModel() != null) {
            requestScope("kintai", KeyFactory.keyToString(loginYago.getKintaiRef().getKey()));
        }
        return forward("edit.jsp");
    }
}
