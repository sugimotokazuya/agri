package agri.controller.kintai;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.kintai.ShuseiShinsei;
import agri.service.kintai.ShuseiShinseiService;

public class ShoninController extends BaseController {

    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    
    @Override
    public Navigation run() throws Exception {
        
        List<ShuseiShinsei> list = ssService.getList(loginYago);
        requestScope("list", list);
        
        return forward("shonin.jsp");
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
}
