package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.shinkoku.TekiyoMeta;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Tekiyo;
import agri.service.shinkoku.TekiyoService;

import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private TekiyoMeta meta = TekiyoMeta.get();
    private TekiyoService service = new TekiyoService();
    
    @Override
    public boolean validateAuth() {
    
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(tekiyo.getKamokuRef().getModel().getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
            requestScope("kamoku", tekiyo.getKamokuRef().getModel());
            return forward("edit.jsp");
        }
        Tekiyo tekiyo = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, tekiyo);
        service.update(tekiyo);
        Kamoku kamoku = tekiyo.getKamokuRef().getModel();
        return redirect(basePath + "?key=" + KeyFactory.keyToString(kamoku.getKey())
            + "&version=" + kamoku.getVersion().toString());
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(10));
        v.add(meta.wariai, v.required(), v.integerType(), v.doubleRange(0, 100));
        return v.validate();
    }
}
