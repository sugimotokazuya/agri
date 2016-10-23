package agri.controller.sagyo.shizai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;
import org.slim3.util.RequestMap;

import agri.controller.BaseController;
import agri.meta.sagyo.ShizaiMeta;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Shizai;
import agri.service.sagyo.ShizaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private ShizaiMeta meta = ShizaiMeta.get();
    private ShizaiService service = new ShizaiService();
    
    @Override
    public boolean validateAuth() {
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        if(!shizai.getSagyoItemRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Shizai shizai = service.get(asKey(meta.key), asLong(meta.version));
        
        if (!validate()) {
            requestScope("sagyoItem", shizai.getSagyoItemRef().getModel());
            return forward("edit.jsp");
        }

        BeanUtil.copy(new RequestMap(request), shizai); 
        service.update(shizai);

        SagyoItem sagyoItem = shizai.getSagyoItemRef().getModel();
        return redirect(basePath + "?key=" + KeyFactory.keyToString(sagyoItem.getKey())
            + "&version=" + sagyoItem.getVersion().toString());
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.tanni, v.maxlength(10));
        v.add(meta.nyushusaki, v.maxlength(20));
        v.add(meta.seizosha, v.maxlength(20));
        v.add(meta.beppyoName, v.maxlength(30));
        v.add(meta.beppyoKubun, v.maxlength(10));
        v.add(meta.riyu, v.maxlength(40));
        return v.validate();
    }
}
