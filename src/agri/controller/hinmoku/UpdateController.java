package agri.controller.hinmoku;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.HinmokuMeta;
import agri.model.Hinmoku;
import agri.service.HinmokuService;

public class UpdateController extends BaseController {

    private HinmokuMeta meta = HinmokuMeta.get();
    private HinmokuService service = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        Hinmoku hinmoku = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(hinmoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthHinmokuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        Hinmoku hinmoku = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, hinmoku);
        hinmoku.setShortName(hinmoku.getName());
        hinmoku.setChokubaiLot(0);
        service.update(hinmoku);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(10));
        v.add(meta.order, v.required(), v.maxlength(3), v.integerType());
        return v.validate();
    }
}
