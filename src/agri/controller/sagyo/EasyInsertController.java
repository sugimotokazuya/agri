package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoMeta;
import agri.model.User;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.Sakuduke;
import agri.service.Const;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.SagyoService;
import agri.service.sagyo.SagyoUserService;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.Key;

public class EasyInsertController extends BaseController {

    private SagyoMeta meta = SagyoMeta.get();
    private SagyoService service = new SagyoService();
    private UserService userService = new UserService();
    private SakudukeService sakudukeService = new SakudukeService();
    private SagyoUserService sagyoUserService = new SagyoUserService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSagyoEdit();
    }
    
    @Override
    public Navigation run() throws Exception {

        List<Sakuduke> list = sakudukeService.getInProgressList(loginYago);
        List<User> userList = userService.getAll(loginYago);
        
        if (!validate(list.size(), userList.size())) {
            return forward("easyCreate");
        }
        
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        
        for(int i = 0; i < list.size(); ++i) {
            Double amount = asDouble("amount" + i);
            if(amount == null) continue;
            Sakuduke sakuduke = sakudukeService.get(asKey("key" + i));
            if(sakuduke == null || !sakuduke.getYagoRef().getModel().equals(loginYago)) continue;
            Sagyo sagyo = new Sagyo();
            sagyo.setDate(cal.getTime());
            sagyo.setAmount(amount);
            sagyo.getSagyoItemRef().setKey(asKey("sagyoItemKey"));
            sagyo.getSakudukeRef().setModel(sakuduke);
            sagyo.getYagoRef().setModel(loginYago);
            Key sagyoKey = service.insert(sagyo);
            
            for(int j = 0; j < userList.size(); ++j) {
                Integer minutes = asInteger("minutes" + i + "_" + j);
                if(minutes == null) continue;
                SagyoUser su = new SagyoUser();
                su.getUserRef().setKey(asKey("user" + i + "_" + j));
                su.getSagyoRef().setKey(sagyoKey);
                su.setMinutes(minutes);
                sagyoUserService.insert(su);
            }
        }
        
        return redirect(basePath);
    }
    
    protected boolean validate(int sakudukeSize, int userSize) {
        Validators v = new Validators(request);
        v.add("sagyoItemKey", v.required(), v.maxlength(Const.KEY_LENGTH));
        
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(2009, cal.get(Calendar.YEAR), "2009年から今年までしか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add(meta.amount, v.maxlength(6), v.doubleType());
        v.add(meta.biko, v.maxlength(1000));
        
        
        for(int i = 0; i < sakudukeSize; ++i) {
            v.add("amount" + i, v.doubleType(), v.maxlength(6));
            for(int j = 0; j < userSize; ++j) {
                v.add("user" + i + "_" + j, v.maxlength(Const.KEY_LENGTH));
                v.add("minutes" + i + "_" + j, v.maxlength(3),  v.integerType());
            }
        }
        
        return v.validate();
    }
}
