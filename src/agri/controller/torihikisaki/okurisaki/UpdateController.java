package agri.controller.torihikisaki.okurisaki;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.hanbai.OkurisakiMeta;
import agri.model.Torihikisaki;
import agri.model.hanbai.Okurisaki;
import agri.service.hanbai.OkurisakiService;

import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private OkurisakiMeta meta = OkurisakiMeta.get();
    private OkurisakiService service = new OkurisakiService();
    
    @Override
    public boolean validateAuth() {
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        if(!okurisaki.getTorihikisakiRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Okurisaki okurisaki = service.get(asKey(meta.key), asLong(meta.version));
        
        if (!validate()) {
            requestScope("torihikisaki", okurisaki.getTorihikisakiRef().getModel());
            return forward("edit.jsp");
        }

        BeanUtil.copy(request, okurisaki); 
        service.update(okurisaki);

        Torihikisaki t = okurisaki.getTorihikisakiRef().getModel();
        return redirect(basePath + "?key=" + KeyFactory.keyToString(t.getKey())
            + "&version=" + t.getVersion().toString());
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(30));
        v.add(meta.address1, v.maxlength(15));
        v.add(meta.address2, v.maxlength(15));
        v.add(meta.address3, v.maxlength(15));
        v.add(meta.yubin1, v.maxlength(3));
        v.add(meta.yubin2, v.maxlength(4));
        v.add(meta.tel, v.maxlength(15));
        v.add(meta.soryo,  v.maxlength(4), v.integerType());
        v.add(meta.haitatsubi, v.maxlength(1), v.doubleRange(1, 2), v.integerType());
        v.add(meta.kiboujikan, v.maxlength(1), v.integerType());
        v.add(meta.iraiName, v.required(), v.maxlength(30));
        v.add(meta.iraiAddress1, v.maxlength(17));
        v.add(meta.iraiAddress2, v.maxlength(17));
        v.add(meta.iraiYubin1, v.maxlength(3));
        v.add(meta.iraiYubin2, v.maxlength(4));
        v.add(meta.iraiTel, v.maxlength(15));
        v.add(meta.iraiName, v.required(), v.maxlength(30));
        v.add(meta.iraiAddress1, v.maxlength(17));
        v.add(meta.iraiAddress2, v.maxlength(17));
        v.add(meta.iraiYubin1, v.maxlength(3));
        v.add(meta.iraiYubin2, v.maxlength(4));
        v.add(meta.iraiTel, v.maxlength(15));
        return v.validate();
    }
}
