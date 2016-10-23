package agri.controller.sagyo.item;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;


public class EditController extends BaseController {

    private SagyoItemMeta meta = SagyoItemMeta.get();
    private SagyoItemService service = new SagyoItemService();
    
    @Override
    public boolean validateAuth() {
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        if(!sagyoItem.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(sagyoItem, request);
        return forward("edit.jsp");
    }
}
