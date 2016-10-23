package agri.controller.sehi.hiryo;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sehi.HiryoMeta;
import agri.model.sehi.Hiryo;
import agri.service.sehi.HiryoService;


public class EditController extends BaseController {

    private HiryoMeta meta = HiryoMeta.get();
    private HiryoService service = new HiryoService();
    
    @Override
    public boolean validateAuth() {
        Hiryo hiryo = service.get(asKey(meta.key), asLong(meta.version));
        if(!hiryo.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Hiryo hiryo = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(hiryo, request);
        return forward("edit.jsp");
    }
}
