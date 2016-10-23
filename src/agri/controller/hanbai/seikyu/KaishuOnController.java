package agri.controller.hanbai.seikyu;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Seikyu;
import agri.service.hanbai.SeikyuService;

public class KaishuOnController extends BaseController {

    private SeikyuService service = new SeikyuService();
    
    @Override
    public boolean validateAuth() {
        Seikyu s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthSeikyuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Seikyu s = service.get(asKey("key"), asLong("version"));
        s.setKaishu(1);
        service.update(s);
        
        return redirect(basePath + "./");
    }
}
