package agri.controller.torihikisaki.okurisaki;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.TorihikisakiMeta;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class CreateController extends BaseController {

    TorihikisakiService service = new TorihikisakiService();
    TorihikisakiMeta meta = TorihikisakiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Torihikisaki t = service.get(asKey(meta.key), asLong(meta.version));
        if(!t.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Torihikisaki t = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("torihikisaki", t);
        
        requestScope("iraiName", t.getIraiName());
        requestScope("iraiAddress1", t.getIraiAddress1());
        requestScope("iraiAddress2", t.getIraiAddress2());
        requestScope("iraiYubin1", t.getIraiYubin1());
        requestScope("iraiYubin2", t.getIraiYubin2());
        requestScope("iraiTel", t.getIraiTel());
        
        return forward("create.jsp");
    }
}
