package agri.controller.yago;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.YagoMeta;
import agri.model.Yago;
import agri.service.UserService;
import agri.service.YagoService;

import com.google.appengine.api.datastore.KeyFactory;


public class EditController extends BaseController {

    private YagoMeta meta = YagoMeta.get();
    private YagoService service = new YagoService();
    private UserService userService = new UserService();
    
    @Override
    public Navigation run() throws Exception {
        
        Yago yago = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(yago, request);
        requestScope("owner", 
            KeyFactory.keyToString(yago.getOwnerRef().getModel().getKey()));
        requestScope("userList", userService.getAll());
        return forward("edit.jsp");
    }
    
    @Override
    protected boolean validateAuth() {
    
        return true;
    }
}
