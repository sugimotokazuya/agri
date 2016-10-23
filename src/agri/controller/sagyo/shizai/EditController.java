package agri.controller.sagyo.shizai;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.ShizaiMeta;
import agri.model.sagyo.Shizai;
import agri.service.sagyo.ShizaiService;

public class EditController extends BaseController {

    private ShizaiMeta meta = ShizaiMeta.get();
    private ShizaiService service = new ShizaiService();
    
    @Override
    public boolean validateAuth() {
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        if(!shizai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("sagyoItem", shizai.getSagyoItemRef().getModel());
        BeanUtil.copy(shizai, request);
        return forward("edit.jsp");
    }
}
