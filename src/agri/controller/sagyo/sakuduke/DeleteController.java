package agri.controller.sagyo.sakuduke;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.exception.UsedAlreadyException;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.Sakuduke;
import agri.service.sagyo.SakudukeService;

public class DeleteController extends BaseController {

    private SakudukeService service = new SakudukeService();
    private SakudukeMeta meta = SakudukeMeta.get();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSakudukeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        try {
            service.delete(asKey(meta.key), asLong(meta.version));
            return redirect(basePath);
        } catch(UsedAlreadyException ex) {
            requestScope("usedAlready", true);
            return forward(basePath);
        }
        
    }
}
