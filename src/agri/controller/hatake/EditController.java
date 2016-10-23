package agri.controller.hatake;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.HatakeMeta;
import agri.model.Hatake;
import agri.service.HatakeService;


public class EditController extends BaseController {

    private HatakeMeta meta = HatakeMeta.get();
    private HatakeService service = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        Hatake hatake = service.get(asKey(meta.key), asLong(meta.version));
        if(!hatake.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthHatakeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Hatake hatake = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(hatake, request);
        return forward("edit.jsp");
    }
}
