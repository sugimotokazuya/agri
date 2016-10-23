package agri.controller.kintai.kinmuhyo;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.model.kintai.Kinmuhyo;
import agri.service.Const;
import agri.service.kintai.KinmuhyoService;

import com.google.appengine.api.datastore.Key;

public class SaveController extends BaseController {

    private KinmuhyoService kService = new KinmuhyoService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return forward("view");
        }
        
        Key key = asKey("key");
        long version = asLong("version");
        
        Kinmuhyo k = kService.get(key, version);
        BeanUtil.copy(request, k);
        k.setEmployeeType(k.getUserRef().getModel().getEmployeeType());
        kService.update(k);
        
        return redirect("/kintai/kinmuhyo");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        v.add("yasumiPlus",v.required(), v.integerType());
        v.add("yasumiZengetsuZan",v.required(), v.integerType());
        v.add("yasumiHour",v.required(), v.integerType());
        v.add("yasumiZan",v.required(), v.integerType());
        v.add("kinmuDays",v.required(), v.integerType());
        v.add("kinmuHours",v.required(), v.integerType());
        v.add("zangyoMinutes",v.required(), v.integerType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
}
