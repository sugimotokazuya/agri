package agri.controller.hinmoku;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.HinmokuMeta;
import agri.model.Hinmoku;
import agri.service.HinmokuService;

public class InsertController extends BaseController {

    private HinmokuMeta meta = HinmokuMeta.get();
    private HinmokuService service = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthHinmokuEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Hinmoku hinmoku = new Hinmoku();
        BeanUtil.copy(request, hinmoku);
        hinmoku.setShortName(hinmoku.getName());
        hinmoku.setChokubaiLot(0);
        hinmoku.getYagoRef().setModel(loginYago);
        service.insert(hinmoku);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(10));
        v.add(meta.order, v.required(), v.maxlength(3), v.integerType());
        return v.validate();
    }
}
