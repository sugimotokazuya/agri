package agri.controller.sagyo.kikai;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.KikaiMeta;
import agri.model.sagyo.Kikai;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.KikaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteController extends BaseController {

    private KikaiService service = new KikaiService();
    private KikaiMeta meta = KikaiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        if(!kikai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        SagyoItem sagyoItem = kikai.getSagyoItemRef().getModel();
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath + "?key=" + KeyFactory.keyToString(sagyoItem.getKey())
            + "&version=" + sagyoItem.getVersion().toString());
    }
}
