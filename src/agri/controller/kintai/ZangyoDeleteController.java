package agri.controller.kintai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.Dakoku;
import agri.service.kintai.DakokuService;

public class ZangyoDeleteController extends BaseController {

    private DakokuService dakokuService = new DakokuService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return redirect("/kintai");
        }
        
        Dakoku dakoku = dakokuService.get(asKey("key"), asLong("version"));
        dakoku.setZangyo(false);
        dakokuService.update(dakoku);
        
        return redirect("/kintai");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("key", v.required(), v.maxlength(50));
        v.add("version", v.required(), v.longType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
}
