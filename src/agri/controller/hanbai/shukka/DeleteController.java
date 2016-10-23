package agri.controller.hanbai.shukka;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Shukka;
import agri.service.hanbai.ShukkaService;

public class DeleteController extends BaseController {

    private ShukkaService service = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        if(s.getKaishu() == 2) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        service.delete(asKey("key"), asLong("version"));
        return redirect(basePath + "../");
    }
}
