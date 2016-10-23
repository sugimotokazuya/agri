package agri.controller.sehi.hiryo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sehi.HiryoMeta;
import agri.model.sehi.Hiryo;
import agri.service.sehi.HiryoService;

public class DeleteController extends BaseController {

    private HiryoService service = new HiryoService();
    private HiryoMeta meta = HiryoMeta.get();
    
    @Override
    public boolean validateAuth() {
        Hiryo hiryo = service.get(asKey(meta.key), asLong(meta.version));
        if(!hiryo.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
