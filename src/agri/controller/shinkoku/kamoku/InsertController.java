package agri.controller.shinkoku.kamoku;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.shinkoku.KamokuMeta;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Tekiyo;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.TekiyoService;

public class InsertController extends BaseController {

    private KamokuMeta meta = KamokuMeta.get();
    private KamokuService service = new KamokuService();
    private TekiyoService tService = new TekiyoService();
    
    @Override
    public boolean validateAuth() {

        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Kamoku kamoku = new Kamoku();
        BeanUtil.copy(request, kamoku);
        kamoku.getYagoRef().setModel(loginYago);
        service.insert(kamoku);
        
        Tekiyo tekiyo = new Tekiyo();
        tekiyo.setName("その他");
        tekiyo.setWariai(100);
        tekiyo.getKamokuRef().setModel(kamoku);
        tService.insert(tekiyo);
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(10));
        v.add(meta.kubun, v.required(), v.integerType(), v.doubleRange(1, 5));
        return v.validate();
    }
}
