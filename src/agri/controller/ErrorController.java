package agri.controller;

import org.slim3.controller.Navigation;

public class ErrorController extends BaseController {

    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        return forward("error.jsp");
    }
 
}
