package agri.controller.shinkoku.kamoku;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.model.shinkoku.Kamoku;
import agri.service.shinkoku.KamokuService;

public class EditController extends BaseController {

    private KamokuMeta meta = KamokuMeta.get();
    private KamokuService service = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(kamoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(kamoku, request);
        return forward("edit.jsp");
    }
}
