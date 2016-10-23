package agri.controller.shinkoku.tekiyo;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.meta.shinkoku.TekiyoMeta;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Tekiyo;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.TekiyoService;

import com.google.appengine.api.datastore.KeyFactory;

public class InsertController extends BaseController {

    private TekiyoMeta meta = TekiyoMeta.get();
    private KamokuMeta kamokuMeta = KamokuMeta.get();
    private TekiyoService service = new TekiyoService();
    private KamokuService kamokuService = new KamokuService();
    
    @Override
    public boolean validateAuth() {
    
        Kamoku kamoku = kamokuService.get(asKey(kamokuMeta.key), asLong(kamokuMeta.version));
        if(!loginYago.equals(kamoku.getYagoRef().getModel()))return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Kamoku kamoku = kamokuService.get(asKey(kamokuMeta.key), asLong(kamokuMeta.version));
        Tekiyo tekiyo = new Tekiyo();
        tekiyo.getKamokuRef().setModel(kamoku);
        tekiyo.setName(asString(meta.name));
        tekiyo.setWariai(asInteger(meta.wariai));
        service.insert(tekiyo);
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
