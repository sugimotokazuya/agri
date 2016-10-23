package agri.controller.yago;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Yago;
import agri.service.YagoService;

import com.google.appengine.api.datastore.Key;

public class ChangeController extends BaseController {

    private YagoService service = new YagoService();
    @Override
    public Navigation run() throws Exception {
        
        Key key = asKey("yago");
        Yago yago = service.get(key);
        sessionScope("superYago", yago);
        
        return forward("/yago");
    }
    
    @Override
    protected boolean validateAuth() {
    
        return true;
    }
}
