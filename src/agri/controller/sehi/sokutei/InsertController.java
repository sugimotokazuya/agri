package agri.controller.sehi.sokutei;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sehi.SokuteiMeta;
import agri.model.sehi.Sokutei;
import agri.service.Util;
import agri.service.sehi.SokuteiService;

public class InsertController extends BaseController {

    private SokuteiMeta meta = SokuteiMeta.get();
    private SokuteiService service = new SokuteiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Sokutei sokutei = new Sokutei();
        BeanUtil.copy(request, sokutei);
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        sokutei.setDate(cal.getTime());
        sokutei.getHatakeRef().setKey(asKey("hatake"));
        sokutei.getYagoRef().setModel(loginYago);
        
        service.insert(sokutei);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(2009, cal.get(Calendar.YEAR), "2009年以降の分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add(meta.hinmoku, v.required(), v.maxlength(15));
        v.add(meta.phH2o, v.maxlength(3), v.doubleType(),v.doubleRange(1, 10));
        v.add(meta.phKcl, v.maxlength(3), v.doubleType(),v.doubleRange(1, 10));
        v.add(meta.seibunNh4n, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunNo3n, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunP, v.maxlength(4), v.integerType(),v.doubleRange(0, 1000));
        v.add(meta.seibunK, v.maxlength(4), v.integerType(),v.doubleRange(0, 1000));
        v.add(meta.seibunCa, v.maxlength(4), v.integerType(),v.doubleRange(0, 1000));
        v.add(meta.seibunMg, v.maxlength(4), v.integerType(),v.doubleRange(0, 1000));
        v.add(meta.seibunEnbun, v.maxlength(5), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunMn, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        v.add(meta.seibunFe, v.maxlength(4), v.doubleType(),v.doubleRange(0, 100));
        return v.validate();
    }
}
