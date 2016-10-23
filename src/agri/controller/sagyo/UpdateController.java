package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoMeta;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.ShiyoKikai;
import agri.model.sagyo.ShiyoShizai;
import agri.service.Const;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.SagyoService;
import agri.service.sagyo.SagyoUserService;
import agri.service.sagyo.ShiyoKikaiService;
import agri.service.sagyo.ShiyoShizaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private SagyoMeta meta = SagyoMeta.get();
    private SagyoService service = new SagyoService();
    private UserService userService = new UserService();
    private ShiyoShizaiService shiyoShizaiService = new ShiyoShizaiService();
    private ShiyoKikaiService shiyoKikaiService = new ShiyoKikaiService();
    private SagyoUserService sagyoUserService = new SagyoUserService();
    
    @Override
    public boolean validateAuth() {
        Sagyo sagyo = service.get(asKey(meta.key), asLong(meta.version));
        if(!sagyo.getSakudukeRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Sagyo sagyo = service.get(asKey(meta.key), asLong(meta.version));
        
        if (!validate()) {
            requestScope("userList", userService.getAll(loginYago));
            requestScope("sagyo", sagyo);
            return forward("edit.jsp");
        }
        
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        sagyo.setDate(cal.getTime());
    
        sagyo.setAmount(asDouble(meta.amount));
        sagyo.setBiko(asString(meta.biko));

        service.update(sagyo);
        
        List<ShiyoShizai> ssList = sagyo.getShiyoShizaiListRef().getModelList();
        for(ShiyoShizai ss : ssList) {
            shiyoShizaiService.delete(ss.getKey());
        }
        List<ShiyoKikai> skList = shiyoKikaiService.getListBySagyo(sagyo);
        for(ShiyoKikai sk : skList) {
            shiyoKikaiService.delete(sk.getKey());
        }
        List<SagyoUser> suList = sagyoUserService.getListBySagyo(sagyo);
        for(SagyoUser su : suList) {
            sagyoUserService.delete(su.getKey());
        }
        
        for(int i = 1; i < 6; ++i) {
            
            // 使用機械
            if(!Util.isEmpty(asString("kikai" + i))) {
                ShiyoKikai sk = new ShiyoKikai();
                sk.getSagyoRef().setKey(sagyo.getKey());
                sk.getKikaiRef().setKey(asKey("kikai" + i));
                shiyoKikaiService.insert(sk);
            }
            
            // 作業者
            if(!Util.isEmpty(asString("user" + i))) {
                SagyoUser su = new SagyoUser();
                su.getUserRef().setKey(asKey("user" + i));
                su.getSagyoRef().setKey(sagyo.getKey());
                su.setMinutes(asInteger("minutes" + i));
                sagyoUserService.insert(su);
            }
            
            // 使用資材
            Double amount = asDouble("amount" + i);
            if(amount == null) amount = new Double(0);
            if(!Util.isEmpty(asString("shizai" + i))) {
                ShiyoShizai shiyoShizai = new ShiyoShizai();
                shiyoShizai.getSagyoRef().setKey(sagyo.getKey());
                shiyoShizai.setAmount(amount);
                shiyoShizai.getShizaiRef().setKey(asKey("shizai" + i));
                shiyoShizaiService.insert(shiyoShizai);
            }
        }
        
        int page = asInteger("page") == null ? 0 : asInteger("page");
        if(page == 0) {
            return redirect(basePath + "view/" + KeyFactory.keyToString(
                sagyo.getSakudukeRef().getKey()));
        } else {
            return redirect(basePath);
        }
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(2009, cal.get(Calendar.YEAR), "2009年から今年までしか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add(meta.amount, v.maxlength(6), v.doubleType());
        v.add(meta.biko, v.maxlength(1000));
        
        for(int i = 1; i < 6; ++i) {
            // 作業者
            v.add("user" + i, v.maxlength(Const.KEY_LENGTH));
            v.add("minutes" + i, v.maxlength(3),  v.integerType());
            // 機械
            v.add("kikai" + i, v.maxlength(Const.KEY_LENGTH));
            //　使用資材
            v.add("shizai" + i,v.maxlength(Const.KEY_LENGTH));
            v.add("amount" + i, v.doubleType(), v.maxlength(6));
        }
        
        return v.validate();
    }
}
