package agri.controller.torihikisaki;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;

public class CreateController extends BaseController {

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if(requestScope("status") == null) {
            requestScope("status", 1);
        }
        return forward("create.jsp");
    }
}
