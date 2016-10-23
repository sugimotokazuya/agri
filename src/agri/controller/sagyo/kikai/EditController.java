package agri.controller.sagyo.kikai;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.KikaiMeta;
import agri.model.sagyo.Kikai;
import agri.service.sagyo.KikaiService;

public class EditController extends BaseController {

    private KikaiMeta meta = KikaiMeta.get();
    private KikaiService service = new KikaiService();
    
    @Override
    public boolean validateAuth() {
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        if(!kikai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("sagyoItem", kikai.getSagyoItemRef().getModel());
        BeanUtil.copy(kikai, request);
        return forward("edit.jsp");
    }
}
