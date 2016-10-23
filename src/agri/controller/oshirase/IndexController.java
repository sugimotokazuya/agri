package agri.controller.oshirase;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.OshiraseService;

public class IndexController extends BaseController {

    private OshiraseService service = new OshiraseService();
    @Override
    public Navigation run() throws Exception {
        requestScope("oshiraseList", service.getAll());
        return forward("index.jsp");
    }
    
    @Override
    public boolean validateAuth() {
        return true;
    }
}
