package agri.controller.torihikisaki;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.TorihikisakiMeta;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class EditController extends BaseController {

    private TorihikisakiMeta meta = TorihikisakiMeta.get();
    private TorihikisakiService service = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        Torihikisaki t = service.get(asKey(meta.key), asLong(meta.version));
        if(!t.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Torihikisaki torihikisaki = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(torihikisaki, request);
        return forward("edit.jsp");
    }
}
