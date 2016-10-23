package agri.controller.chokubai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.ChokubaiMeta;
import agri.model.Chokubai;
import agri.model.Torihikisaki;
import agri.service.ChokubaiService;
import agri.service.TorihikisakiService;

import com.google.appengine.api.datastore.Key;

public class InsertController extends BaseController {

    private ChokubaiMeta meta = ChokubaiMeta.get();
    private TorihikisakiService tService = new TorihikisakiService();
    private ChokubaiService cService = new ChokubaiService();
    
    @Override
    public boolean validateAuth() {
        Key key = asKey("torihikisakiRef");
        Torihikisaki t = tService.get(key);
        if(!loginYago.equals(t.getYagoRef().getModel())) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Chokubai c = new Chokubai();
        BeanUtil.copy(request, c);
        c.getTorihikisakiRef().setKey(asKey("torihikisakiRef"));
        cService.insert(c);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(15));
        v.add(meta.order,v.required(), v.integerType(), v.maxlength(3));
        return v.validate();
    }
}
