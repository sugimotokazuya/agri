package agri.controller.sehi.sokutei;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sehi.SokuteiMeta;
import agri.model.sehi.Sokutei;
import agri.service.sehi.SokuteiService;

public class DeleteController extends BaseController {

    private SokuteiService service = new SokuteiService();
    private SokuteiMeta meta = SokuteiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Sokutei sokutei = service.get(asKey(meta.key), asLong(meta.version));
        if(!sokutei.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
