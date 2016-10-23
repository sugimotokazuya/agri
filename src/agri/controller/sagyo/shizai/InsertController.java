package agri.controller.sagyo.shizai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;
import org.slim3.util.RequestMap;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.meta.sagyo.ShizaiMeta;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Shizai;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.ShizaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class InsertController extends BaseController {

    private ShizaiMeta meta = ShizaiMeta.get();
    private SagyoItemMeta sagyoItemMeta = SagyoItemMeta.get();
    private ShizaiService service = new ShizaiService();
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
        SagyoItem sagyoItem = siService.get(asKey(sagyoItemMeta.key), asLong(sagyoItemMeta.version));
        Shizai shizai = new Shizai();
        BeanUtil.copy(new RequestMap(request), shizai);
        shizai.setKey(null);
        shizai.setVersion(null);
        shizai.getSagyoItemRef().setModel(sagyoItem);
        service.insert(shizai);
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
