package agri.controller.hatake;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.HatakeMeta;
import agri.model.Hatake;
import agri.service.HatakeService;

public class DeleteController extends BaseController {

    private HatakeService service = new HatakeService();
    private HatakeMeta meta = HatakeMeta.get();
    
    @Override
    public boolean validateAuth() {
        Hatake hatake = service.get(asKey(meta.key), asLong(meta.version));
        if(!hatake.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthHatakeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {

        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
