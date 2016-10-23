package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.shinkoku.TekiyoMeta;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Tekiyo;
import agri.service.shinkoku.TekiyoService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteController extends BaseController {

    private TekiyoService service = new TekiyoService();
    private TekiyoMeta meta = TekiyoMeta.get();

    @Override
    public boolean validateAuth() {
    
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(tekiyo.getKamokuRef().getModel().getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        Kamoku kamoku = tekiyo.getKamokuRef().getModel();
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath + "?key=" + KeyFactory.keyToString(kamoku.getKey())
            + "&version=" + kamoku.getVersion().toString());
    }
}
