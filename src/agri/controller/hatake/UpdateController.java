package agri.controller.hatake;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.HatakeMeta;
import agri.model.Hatake;
import agri.service.Const;
import agri.service.HatakeService;
import agri.service.Util;

public class UpdateController extends BaseController {

    private HatakeMeta meta = HatakeMeta.get();
    private HatakeService service = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        Hatake hatake = service.get(asKey(meta.key), asLong(meta.version));
        if(!hatake.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthHatakeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        Hatake hatake = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, hatake);
        if(asInteger("year") != null && asInteger("month") != null && asInteger("day") !=null) {
            Calendar cal = Util.getCalendar();
            Util.resetTime(cal);
            cal.set(Calendar.YEAR, asInteger("year"));
            cal.set(Calendar.MONTH, asInteger("month") - 1);
            cal.set(Calendar.DAY_OF_MONTH, asInteger("day"));
            hatake.setStartYuhki(cal.getTime());
        }
        
        service.update(hatake);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        v.add(meta.no, v.required(), v.integerType());
        v.add(meta.name, v.required(), v.maxlength(10));
        v.add(meta.shortName, v.required(), v.maxlength(3));
        v.add(meta.address, v.required(), v.maxlength(50));
        v.add(meta.area, v.required(), v.maxlength(4), v.doubleType(), v.doubleRange(0, 100));
        Calendar cal = Util.getCalendar();
        v.add("year",  
            v.longRange(1900, cal.get(Calendar.YEAR), "1900年から今年までしか入力できません。"));
        v.add("month", v.longRange(1, 12));
        v.add("day", v.longRange(1, 31));
        return v.validate();
    }
}
