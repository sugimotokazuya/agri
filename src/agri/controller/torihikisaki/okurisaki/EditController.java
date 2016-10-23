package agri.controller.torihikisaki.okurisaki;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.hanbai.OkurisakiMeta;
import agri.model.hanbai.Okurisaki;
import agri.service.hanbai.OkurisakiService;

public class EditController extends BaseController {

    private OkurisakiMeta meta = OkurisakiMeta.get();
    private OkurisakiService service = new OkurisakiService();
    
    @Override
    public boolean validateAuth() {
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        if(!okurisaki.getTorihikisakiRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("torihikisaki", okurisaki.getTorihikisakiRef().getModel());
        BeanUtil.copy(okurisaki, request);
        return forward("edit.jsp");
    }
}
