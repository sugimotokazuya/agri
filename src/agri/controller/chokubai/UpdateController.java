package agri.controller.chokubai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.ChokubaiMeta;
import agri.model.Chokubai;
import agri.model.Yago;
import agri.service.ChokubaiService;

public class UpdateController extends BaseController {

    private ChokubaiMeta meta = ChokubaiMeta.get();
    private ChokubaiService service = new ChokubaiService();
    
    @Override
    public boolean validateAuth() {
        Chokubai chokubai = service.get(asKey(meta.key), asLong(meta.version));
        Yago yago = chokubai.getTorihikisakiRef().getModel().getYagoRef().getModel();
        if(!loginYago.equals(yago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        Chokubai c = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, c);
        service.update(c);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(15));
        v.add(meta.order,v.required(), v.integerType(), v.maxlength(3));
        return v.validate();
    }
}
