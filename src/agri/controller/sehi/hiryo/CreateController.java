package agri.controller.sehi.hiryo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;

public class CreateController extends BaseController {

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        return forward("create.jsp");
    }
}
