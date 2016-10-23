package agri.controller.hanbai.keitai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.hanbai.ShukkaKeitaiService;

public class UpdateController extends BaseController {

    private ShukkaKeitaiMeta meta = ShukkaKeitaiMeta.get();
    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    
    @Override
    public boolean validateAuth() {
        ShukkaKeitai p = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(p.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        ShukkaKeitai p = service.get(asKey(meta.key), asLong(meta.version));
        if (!validate()) {
            requestScope("torihikisakiRef", p.getTorihikisakiRef());
            requestScope("hinmokuRef", p.getHinmokuRef());
            return forward("edit.jsp");
        }
        
        BeanUtil.copy(request, p);
        service.update(p);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.juryo, v.required(), v.maxlength(6), v.integerType());
        v.add(meta.price, v.required(), v.maxlength(6), v.integerType());
        v.add(meta.hoso, v.required(), v.maxlength(10));
        return v.validate();
    }
}
