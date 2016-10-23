package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.model.shinkoku.Kamoku;
import agri.service.shinkoku.KamokuService;

public class CreateController extends BaseController {

    KamokuService service = new KamokuService();
    KamokuMeta meta = KamokuMeta.get();
    
    @Override
    public boolean validateAuth() {
    
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(kamoku.getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        requestScope("kamoku", service.get(asKey(meta.key), asLong(meta.version)));
        return forward("create.jsp");
    }
}
