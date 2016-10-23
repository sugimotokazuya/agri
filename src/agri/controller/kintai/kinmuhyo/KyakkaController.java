package agri.controller.kintai.kinmuhyo;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.Kinmuhyo;
import agri.service.kintai.KinmuhyoService;

import com.google.appengine.api.datastore.Key;

public class KyakkaController extends BaseController {

    private KinmuhyoService kService = new KinmuhyoService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return forward("view");
        }
        
        Key key = asKey("key");
        long version = asLong("version");
        
        Kinmuhyo k = kService.get(key, version);
        k.setShonin(3);
        k.setRiyu(asString("riyu"));
        kService.update(k);
        
        return redirect("/kintai/kinmuhyo");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("key", v.required(), v.maxlength(50));
        v.add("version", v.required(), v.longType());
        v.add("riyu", v.required(), v.maxlength(100));
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
}
