package agri.controller.hinmoku;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.HinmokuMeta;
import agri.model.Hinmoku;
import agri.service.HinmokuService;

public class DeleteController extends BaseController {

    private HinmokuService service = new HinmokuService();
    private HinmokuMeta meta = HinmokuMeta.get();
    
    @Override
    public boolean validateAuth() {
        Hinmoku h = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(h.getYagoRef().getModel())) return false;
        return loginUser.isAuthHinmokuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
