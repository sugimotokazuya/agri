package agri.controller.oshirase;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.OshiraseMeta;
import agri.model.Oshirase;
import agri.service.OshiraseService;
import agri.service.Util;

public class InsertController extends BaseController {

    private OshiraseMeta meta = OshiraseMeta.get();
    private OshiraseService service = new OshiraseService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        
        Oshirase oshirase = new Oshirase();
        BeanUtil.copy(request, oshirase);
        oshirase.setDate(cal.getTime());
        service.insert(oshirase);
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR), cal.get(Calendar.YEAR) + 1, "本年と翌年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        
        v.add(meta.header, v.required(), v.maxlength(20));
        v.add(meta.body, v.required(), v.maxlength(250));
        return v.validate();
    }
}
