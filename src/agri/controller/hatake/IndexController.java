package agri.controller.hatake;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.HatakeService;

public class IndexController extends BaseController {

    
    private HatakeService service = new HatakeService();

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthHatakeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        requestScope("hatakeList", service.getAll(loginYago));
        return forward("index.jsp");
    }
}
