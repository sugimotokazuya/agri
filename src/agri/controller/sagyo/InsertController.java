package agri.controller.sagyo;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoMeta;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.Sakuduke;
import agri.model.sagyo.ShiyoKikai;
import agri.model.sagyo.ShiyoShizai;
import agri.service.Const;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.GazoService;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SagyoService;
import agri.service.sagyo.SagyoUserService;
import agri.service.sagyo.SakudukeService;
import agri.service.sagyo.ShiyoKikaiService;
import agri.service.sagyo.ShiyoShizaiService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InsertController extends BaseController {

    private SagyoMeta meta = SagyoMeta.get();
    private SagyoService service = new SagyoService();
    private UserService userService = new UserService();
    private SagyoItemService sagyoItemService = new SagyoItemService();
    private SakudukeService sakudukeService = new SakudukeService();
    private ShiyoShizaiService shiyoShizaiService = new ShiyoShizaiService();
    private SagyoUserService sagyoUserService = new SagyoUserService();
    private ShiyoKikaiService shiyoKikaiService = new ShiyoKikaiService();
    private GazoService gazoService = new GazoService();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = sakudukeService.get(asKey("sakudukeKey"));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            initCreate();
            return forward("create.jsp");
        }
        Sagyo sagyo = new Sagyo();
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        sagyo.setDate(cal.getTime());
        
        sagyo.setAmount(asDouble(meta.amount));
        sagyo.setBiko(asString(meta.biko));
        
        sagyo.getSagyoItemRef().setKey(asKey("sagyoItemKey"));
        sagyo.getSakudukeRef().setKey(asKey("sakudukeKey"));
        sagyo.getYagoRef().setModel(loginYago);
        
        Key sagyoKey = service.insert(sagyo);
        
        for(int i = 1; i < 6; ++i) {
            
            // 使用機械
            if(!Util.isEmpty(asString("kikai" + i))) {
                ShiyoKikai sk = new ShiyoKikai();
                sk.getSagyoRef().setKey(sagyoKey);
                sk.getKikaiRef().setKey(asKey("kikai" + i));
                shiyoKikaiService.insert(sk);
            }
            
            // 作業者
            if(!Util.isEmpty(asString("user" + i))) {
                SagyoUser su = new SagyoUser();
                su.getUserRef().setKey(asKey("user" + i));
                su.getSagyoRef().setKey(sagyoKey);
                su.setMinutes(asInteger("minutes" + i));
                sagyoUserService.insert(su);
            }
            
            // 使用資材
            Double amount = asDouble("amount" + i);
            if(amount == null) amount = new Double(0);
            if(!Util.isEmpty(asString("shizai" + i))) {
                ShiyoShizai shiyoShizai = new ShiyoShizai();
                shiyoShizai.getSagyoRef().setKey(sagyoKey);
                shiyoShizai.setAmount(amount);
                shiyoShizai.getShizaiRef().setKey(asKey("shizai" + i));
                shiyoShizaiService.insert(shiyoShizai);
            }
        }
        
        if(!Util.isEmpty(asString("gazoHidden"))) {
            String[] keyStrs = asString("gazoHidden").split(",");
            for(int i = 0; i < keyStrs.length; ++i) {
                Key gazoKey = KeyFactory.stringToKey(keyStrs[i]);
                Gazo gazo = gazoService.get(gazoKey);
                gazo.getSagyoRef().setModel(sagyo);
                gazo.setUse(true);
                gazoService.update(gazo);
            }
        }
        
        if(asInteger("continue") == 1) {
            initCreate();
            return forward("create.jsp");
        } else {
            return redirect(basePath);
        }
    }
    
    private void initCreate() {
        requestScope("userList", userService.getAll(loginYago));
        SagyoItem sagyoItem = sagyoItemService.get(asKey("sagyoItemKey"));
        requestScope("sagyoItem", sagyoItem);
        requestScope("sagyoItemKey", sagyoItem.getKey());
        
        Sakuduke sakuduke = sakudukeService.get(asKey("sakudukeKey"));
        requestScope("sakuduke", sakuduke);
        requestScope("sakudukeKey", sakuduke.getKey());
    
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("sakudukeKey", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("sagyoItemKey", v.required(), v.maxlength(Const.KEY_LENGTH));
        
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
