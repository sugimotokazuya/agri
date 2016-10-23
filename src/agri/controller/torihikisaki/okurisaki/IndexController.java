package agri.controller.torihikisaki.okurisaki;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.TorihikisakiMeta;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class IndexController extends BaseController {

    private TorihikisakiService service = new TorihikisakiService();
    private TorihikisakiMeta meta = TorihikisakiMeta.get();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthTorihikisakiView();
    }
    
    @Override
    public Navigation run() throws Exception {
        Torihikisaki t = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("torihikisaki", t);
        requestScope("list", t.getOkurisakiListRef().getModelList());
        return forward("index.jsp");
    }
}
