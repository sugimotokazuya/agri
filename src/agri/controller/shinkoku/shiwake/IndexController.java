package agri.controller.shinkoku.shiwake;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.ShiwakeService;

public class IndexController extends BaseController {

    private KamokuService service = new KamokuService();
    private ShiwakeService shiwakeService = new ShiwakeService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        List<Kamoku> kamokuList = service.getAll(loginYago);
        List<Shiwake> shiwakeList = shiwakeService.getLast100(loginYago);
        requestScope("kamokuList", kamokuList);
        requestScope("shiwakeList", shiwakeList);
        
        if(sessionScope("karikataKamoku") != null) requestScope("karikataKamoku", sessionScope("karikataKamoku"));
        if(sessionScope("kashikataKamoku") != null) requestScope("kashikataKamoku", sessionScope("kashikataKamoku"));
        
        return forward("index.jsp");
    }
}
