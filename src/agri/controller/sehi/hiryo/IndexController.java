package agri.controller.sehi.hiryo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.sehi.HiryoService;

public class IndexController extends BaseController {

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiView();
    }
    
    private HiryoService service = new HiryoService();
    @Override
    public Navigation run() throws Exception {
        requestScope("hiryoList", service.getAll(loginYago));
        return forward("index.jsp");
    }
}
