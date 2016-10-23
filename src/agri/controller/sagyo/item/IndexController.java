package agri.controller.sagyo.item;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.sagyo.SagyoItemService;

public class IndexController extends BaseController {
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSagyoItemView();
    }

    private SagyoItemService service = new SagyoItemService();
    @Override
    public Navigation run() throws Exception {
        requestScope("sagyoItemList", service.getAll(loginYago));
        return forward("index.jsp");
    }
}
