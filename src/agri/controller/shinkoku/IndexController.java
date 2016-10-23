package agri.controller.shinkoku;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;

public class IndexController extends BaseController {

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        return forward("index.jsp");
    }
}
