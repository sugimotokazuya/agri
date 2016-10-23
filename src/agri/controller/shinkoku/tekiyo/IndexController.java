package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.model.shinkoku.Kamoku;
import agri.service.shinkoku.KamokuService;

public class IndexController extends BaseController {

    private KamokuService service = new KamokuService();
    private KamokuMeta meta = KamokuMeta.get();
    
    @Override
    public boolean validateAuth() {
    
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(kamoku.getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("kamoku", kamoku);
        requestScope("tekiyoList", kamoku.getTekiyoListRef().getModelList());
        return forward("index.jsp");
    }
}
