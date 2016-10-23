package agri.controller.hinmoku;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.HinmokuService;

public class IndexController extends BaseController {

    
    private HinmokuService service = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthHinmokuView();
    }
    
    @Override
    public Navigation run() throws Exception {
        requestScope("hinmokuList", service.getAllWithDeleted(loginYago));
        return forward("index.jsp");
    }
}
