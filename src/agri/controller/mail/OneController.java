package agri.controller.mail;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.mail.RecMailMeta;
import agri.model.mail.RecMail;
import agri.service.mail.RecMailService;

import com.google.appengine.api.datastore.Key;

public class OneController extends BaseController {

    private RecMailService service = new RecMailService();
    private RecMailMeta meta = RecMailMeta.get();
    
    @Override
    public boolean validateAuth() {
        RecMail rm = service.get(asKey(meta.key), asLong(meta.version));
        if(!rm.getYagoRef().getModel().equals(loginYago)) {
            return false;
        }
        return loginUser.isAuthMailEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Key key = asKey(meta.key);
        Long version = asLong(meta.version);
        
        RecMail rm = service.get(key, version);
        requestScope("rm", rm);
        
        return forward("one.jsp");
    }
}
