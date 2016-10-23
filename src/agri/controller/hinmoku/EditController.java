package agri.controller.hinmoku;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.HinmokuMeta;
import agri.model.Hinmoku;
import agri.service.HinmokuService;

public class EditController extends BaseController {

    private HinmokuMeta meta = HinmokuMeta.get();
    private HinmokuService service = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        Hinmoku hinmoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(hinmoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthHinmokuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Hinmoku hinmoku = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(hinmoku, request);
        return forward("edit.jsp");
    }
}
