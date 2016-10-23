package agri.controller.chokubai;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class CreateController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        List<Torihikisaki> tList = tService.getChokubai(loginYago);
        requestScope("tList", tList);
        return forward("create.jsp");
    }
}
