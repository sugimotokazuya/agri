package agri.controller.sehi.hiryo;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sehi.HiryoMeta;
import agri.model.sehi.Hiryo;
import agri.service.sehi.HiryoService;

public class InsertController extends BaseController {

    private HiryoMeta meta = HiryoMeta.get();
    private HiryoService service = new HiryoService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Hiryo hiryo = new Hiryo();
        BeanUtil.copy(request, hiryo);
        hiryo.getYagoRef().setModel(loginYago);
        service.insert(hiryo);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.nkeisu, v.maxlength(3), v.integerType(),v.doubleRange(0, 100));
        v.add(meta.seibunN, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunP, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunK, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunCa, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunMg, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunB, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunMn, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunFe, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.weight, v.required(), v.maxlength(4), v.integerType(),v.doubleRange(1, 9999));
        return v.validate();
    }
}
