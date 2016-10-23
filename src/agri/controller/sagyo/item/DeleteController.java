package agri.controller.sagyo.item;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;

public class DeleteController extends BaseController {

    private SagyoItemService service = new SagyoItemService();
    private SagyoItemMeta meta = SagyoItemMeta.get();
    
    @Override
    public boolean validateAuth() {
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        if(!sagyoItem.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
