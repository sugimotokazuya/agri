package agri.controller.sagyo.sakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.Hatake;
import agri.model.User;
import agri.model.sagyo.Sakuduke;
import agri.service.Const;
import agri.service.HatakeService;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

public class InsertController extends BaseController {

    private SakudukeMeta meta = SakudukeMeta.get();
    private SakudukeService service = new SakudukeService();
    private HatakeService hatakeService = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            List<Hatake> hatakeList = hatakeService.getAll(loginYago);
            requestScope("hatakeList", hatakeList);
            List<User> userList = userService.getAll(loginYago);
            requestScope("userList", userList);
            return forward("create.jsp");
        }
        Sakuduke sakuduke = new Sakuduke();
        BeanUtil.copy(request, sakuduke);
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        sakuduke.setStartDate(cal.getTime());
        sakuduke.setEasyInput(asInteger("easyInput"));
        sakuduke.getHatakeRef().setKey(asKey("hatake"));
        sakuduke.getTantoRef().setKey(asKey("tanto"));
        sakuduke.getAdminUserRef().setKey(asKey("adminUser"));
        sakuduke.getYagoRef().setModel(loginYago);
        service.insert(sakuduke);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        
        Validators v = new Validators(request);
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
