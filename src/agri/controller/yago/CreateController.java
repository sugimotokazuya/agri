package agri.controller.yago;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.UserService;

public class CreateController extends BaseController {

    private UserService userService = new UserService();
    
    @Override
    public Navigation run() throws Exception {
        requestScope("userList", userService.getAll());
        return forward("create.jsp");
    }
    
    @Override
    protected boolean validateAuth() {
    
        return true;
    }
}
