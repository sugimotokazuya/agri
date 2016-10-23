package agri.controller.sagyo.kikai;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;

public class CreateController extends BaseController {

    SagyoItemService service = new SagyoItemService();
    SagyoItemMeta meta = SagyoItemMeta.get();
    
    @Override
    public boolean validateAuth() {
        SagyoItem si = service.get(asKey(meta.key), asLong(meta.version));
        if(!si.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        requestScope("sagyoItem", service.get(asKey(meta.key), asLong(meta.version)));
        return forward("create.jsp");
    }
}
