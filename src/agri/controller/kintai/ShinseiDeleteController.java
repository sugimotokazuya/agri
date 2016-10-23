package agri.controller.kintai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.ShuseiShinsei;
import agri.service.kintai.ShuseiShinseiService;

public class ShinseiDeleteController extends BaseController {

    private ShuseiShinseiService ssService = new ShuseiShinseiService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return redirect("/kintai");
        }
        
        ssService.delete(asKey("key"), asLong("version"));
        
        return redirect("/kintai");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("key", v.maxlength(50));
        v.add("version", v.longType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        ShuseiShinsei ss = ssService.get(asKey("key"), asLong("version"));
        if(!ss.getUserRef().getModel().equals(loginUser)) return false;
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
    }
}
