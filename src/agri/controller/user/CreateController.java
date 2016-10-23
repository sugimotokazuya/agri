package agri.controller.user;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;

public class CreateController extends BaseController {

    
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthUserEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        return forward("create.jsp");
    }
}
