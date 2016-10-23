package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.shinkoku.TekiyoMeta;
import agri.model.shinkoku.Tekiyo;
import agri.service.shinkoku.TekiyoService;

public class EditController extends BaseController {

    private TekiyoMeta meta = TekiyoMeta.get();
    private TekiyoService service = new TekiyoService();
    
    @Override
    public boolean validateAuth() {
    
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(tekiyo.getKamokuRef().getModel().getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("kamoku", tekiyo.getKamokuRef().getModel());
        BeanUtil.copy(tekiyo, request);
        return forward("edit.jsp");
    }
}
