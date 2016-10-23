package agri.controller.torihikisaki.okurisaki;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.hanbai.OkurisakiMeta;
import agri.model.Torihikisaki;
import agri.model.hanbai.Okurisaki;
import agri.service.hanbai.OkurisakiService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteController extends BaseController {

    private OkurisakiService service = new OkurisakiService();
    private OkurisakiMeta meta = OkurisakiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        if(!okurisaki.getTorihikisakiRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        Torihikisaki t = okurisaki.getTorihikisakiRef().getModel();
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath + "?key=" + KeyFactory.keyToString(t.getKey())
            + "&version=" + t.getVersion().toString());
    }
}
