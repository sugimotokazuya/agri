package agri.controller.sagyo.kikai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.sagyo.KikaiMeta;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.Kikai;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.KikaiService;
import agri.service.sagyo.SagyoItemService;

import com.google.appengine.api.datastore.KeyFactory;

public class InsertController extends BaseController {

    private KikaiMeta meta = KikaiMeta.get();
    private SagyoItemMeta sagyoItemMeta = SagyoItemMeta.get();
    private KikaiService service = new KikaiService();
    private SagyoItemService siService = new SagyoItemService();
    
    @Override
    public boolean validateAuth() {
        SagyoItem si = siService.get(asKey(sagyoItemMeta.key), asLong(sagyoItemMeta.version));
        if(!si.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        SagyoItem sagyoItem = new SagyoItem();
        sagyoItem.setKey(asKey(sagyoItemMeta.key));
        sagyoItem.setVersion(asLong(sagyoItemMeta.version));
        Kikai kikai = new Kikai();
        kikai.getSagyoItemRef().setModel(sagyoItem);
        kikai.setName(asString(meta.name));
        kikai.setOsenboushisochi(asString(meta.osenboushisochi));
        service.insert(kikai);
        return redirect(basePath + "?key=" + KeyFactory.keyToString(sagyoItem.getKey())
            + "&version=" + sagyoItem.getVersion().toString());
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.osenboushisochi, v.maxlength(20));
        return v.validate();
    }
}
