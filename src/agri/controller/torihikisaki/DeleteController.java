package agri.controller.torihikisaki;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.TorihikisakiMeta;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class DeleteController extends BaseController {

    private TorihikisakiService service = new TorihikisakiService();
    private TorihikisakiMeta meta = TorihikisakiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Torihikisaki t = service.get(asKey(meta.key), asLong(meta.version));
        if(!t.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
