package agri.controller.user;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.UserService;

public class IndexController extends BaseController {

    private UserService service = new UserService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthUserView();
    }
    
    @Override
    public Navigation run() throws Exception {
       
        requestScope("owner", loginYago.getOwnerRef().getModel());
        requestScope("userList", service.getAllWithDeleted(loginYago));
        return forward("index.jsp");
    }
}
