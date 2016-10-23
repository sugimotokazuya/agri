package agri.controller.hanbai.seikyu;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Seikyu;
import agri.service.hanbai.SeikyuService;

public class DeleteController extends BaseController {

    private SeikyuService service = new SeikyuService();
    
    @Override
    public boolean validateAuth() {
    
        Seikyu s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthSeikyuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        service.delete(asKey("key"), asLong("version"));
        return redirect(basePath + "../");
    }
}
