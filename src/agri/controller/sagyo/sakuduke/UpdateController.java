package agri.controller.sagyo.sakuduke;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.Sakuduke;
import agri.service.Const;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

public class UpdateController extends BaseController {

    private SakudukeMeta meta = SakudukeMeta.get();
    private SakudukeService service = new SakudukeService();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSakudukeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        Sakuduke sakuduke = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, sakuduke);
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        sakuduke.setStartDate(cal.getTime());
        sakuduke.setEasyInput(asInteger("easyInput"));
        sakuduke.getHatakeRef().setKey(asKey("hatake"));
        sakuduke.getTantoRef().setKey(asKey("tanto"));
        sakuduke.getAdminUserRef().setKey(asKey("adminUser"));
        service.update(sakuduke);
        return redirect("/sagyo/sakuduke");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(2009, cal.get(Calendar.YEAR), "2013年以降の分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.status, v.integerType(), v.doubleRange(0, 1));
        v.add(meta.easyInput, v.integerType(), v.doubleRange(0, 1));
        v.add(meta.area, v.required(), v.maxlength(4), v.doubleType(), v.doubleRange(0, 100));
        v.add("hatake", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("tanto", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("adminUser", v.required(), v.maxlength(Const.KEY_LENGTH));
        return v.validate();
    }
}
