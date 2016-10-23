package agri.controller.sagyo.kikai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;
import org.slim3.util.RequestMap;

import agri.controller.BaseController;
import agri.meta.sagyo.KikaiMeta;
import agri.model.sagyo.Kikai;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.KikaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private KikaiMeta meta = KikaiMeta.get();
    private KikaiService service = new KikaiService();
    
    @Override
    public boolean validateAuth() {
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        if(!kikai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Kikai kikai = service.get(asKey(meta.key), asLong(meta.version));
        
        if (!validate()) {
            requestScope("sagyoItem", kikai.getSagyoItemRef().getModel());
            return forward("edit.jsp");
        }

        BeanUtil.copy(new RequestMap(request), kikai); 
        service.update(kikai);

        SagyoItem sagyoItem = kikai.getSagyoItemRef().getModel();
        return redirect(basePath + "?key=" + KeyFactory.keyToString(sagyoItem.getKey())
            + "&version=" + sagyoItem.getVersion().toString());
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.osenboushisochi, v.required(), v.maxlength(10));
        return v.validate();
    }
}
