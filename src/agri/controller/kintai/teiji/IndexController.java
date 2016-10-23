package agri.controller.kintai.teiji;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.kintai.Teiji;
import agri.service.kintai.TeijiService;

public class IndexController extends BaseController {

    private TeijiService teijiService = new TeijiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<Teiji> list = teijiService.getList(loginYago);
        requestScope("list", list);
        
        return forward("index.jsp");
    }
}
