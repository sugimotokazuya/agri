package agri.controller.shinkoku.kamoku;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.shinkoku.KamokuService;

public class IndexController extends BaseController {

    private KamokuService service = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        requestScope("kamokuList", service.getAll(loginYago));
        return forward("index.jsp");
    }
}
