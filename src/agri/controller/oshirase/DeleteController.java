package agri.controller.oshirase;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.OshiraseMeta;
import agri.service.OshiraseService;

public class DeleteController extends BaseController {

    private OshiraseService service = new OshiraseService();
    private OshiraseMeta meta = OshiraseMeta.get();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
