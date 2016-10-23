package agri.controller.shinkoku.kamoku;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.model.shinkoku.Kamoku;
import agri.service.shinkoku.KamokuService;

public class UpdateController extends BaseController {

    private KamokuMeta meta = KamokuMeta.get();
    private KamokuService service = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(kamoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        
        Kamoku kamoku = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, kamoku);
        kamoku.getYagoRef().setModel(loginYago);
        service.update(kamoku);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(10));
        return v.validate();
    }
}
