package agri.controller.yago;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.YagoService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class IndexController extends BaseController {

    private YagoService service = new YagoService();
    @Override
    public Navigation run() throws Exception {
        
        String keyStr = asString("yago");
        Key yagoKey;
        if(keyStr == null) {
            yagoKey = loginYago.getKey();
        } else {
            yagoKey = KeyFactory.stringToKey(keyStr);
        }
        requestScope("yago", KeyFactory.keyToString(yagoKey));
        
        requestScope("yagoList", service.getAllWithDeleted());
        return forward("index.jsp");
    }
    
    @Override
    protected boolean validateAuth() {
    
        return true;
    }
}
