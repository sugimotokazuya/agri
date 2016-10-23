package agri.controller.sagyo.shizai;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.ShizaiMeta;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Shizai;
import agri.service.sagyo.ShizaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteController extends BaseController {

    private ShizaiService service = new ShizaiService();
    private ShizaiMeta meta = ShizaiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        if(!shizai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        SagyoItem sagyoItem = shizai.getSagyoItemRef().getModel();
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath + "?key=" + KeyFactory.keyToString(sagyoItem.getKey())
            + "&version=" + sagyoItem.getVersion().toString());
    }
}
