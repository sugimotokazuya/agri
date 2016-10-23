package agri.controller.hanbai.kakuduke;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.hanbai.Kakuduke;
import agri.service.Const;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;

public class InsertController extends BaseController {

    private KakudukeService kService = new KakudukeService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            List<User> list = userService.getKakudukeTantoList(loginYago);
            requestScope("list", list);
            return forward("create.jsp");
        }
        
        Kakuduke k = new Kakuduke();
        k.getUserRef().setKey(asKey("userRef"));
        k.setBiko(asString("biko"));
        int plus = asInteger("plus");
        if(asInteger("kind") == 1) plus *= -1;
        k.setPlus(plus);
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        k.setDate(cal.getTime());
        k.getYagoRef().setModel(loginYago);
        kService.insert(k);
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        
        v.add("userRef", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("kind", v.required(), v.maxlength(1),v.integerType(), v.doubleRange(0, 1));
        v.add("plus", v.required(), v.maxlength(7), v.integerType(), v.doubleRange(0, 999999));
        v.add("biko", v.required(), v.maxlength(10));
        return v.validate();
    }

}
