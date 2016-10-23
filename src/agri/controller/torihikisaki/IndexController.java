package agri.controller.torihikisaki;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class IndexController extends BaseController {

    private TorihikisakiService service = new TorihikisakiService();
    private static String ALL = "torihikisaki.all";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthTorihikisakiView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Boolean allB = asBoolean("all") == null ? (Boolean) sessionScope(ALL) : asBoolean("all");
        if(allB == null) allB = false;
        List<Torihikisaki> list;
        if(allB) {
            list = service.getAllWithDeleted(loginYago);
        } else {
            list= service.getAll(loginYago);
        }
        sessionScope(ALL, allB);
        requestScope("all", allB);
        requestScope("torihikisakiList", list);
        return forward("index.jsp");
    }
}
