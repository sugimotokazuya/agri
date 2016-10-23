package agri.controller.settei;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.YagoMeta;
import agri.model.User;
import agri.service.Const;
import agri.service.UserService;
import agri.service.Util;
import agri.service.YagoService;

public class UpdateController extends BaseController {

    private YagoMeta meta = YagoMeta.get();
    private YagoService service = new YagoService();
    private UserService userService = new UserService();
    
    @Override
    public boolean validateAuth() {
        return loginYago.getOwnerRef().getModel().equals(loginUser);
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        
        BeanUtil.copy(request, loginYago);
        if(!Util.isEmpty(asString("kintai"))) {
            User kintaiUser = userService.get(asKey("kintai"));
            loginYago.getKintaiRef().setModel(kintaiUser);
        }
        service.update(loginYago);
        return redirect("/");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(30));
        v.add("kintai", v.maxlength(Const.KEY_LENGTH));    
        v.add(meta.toiawase,  v.maxlength(200));
        v.add(meta.furikomisaki1, v.maxlength(200));
        v.add(meta.furikomisaki2, v.maxlength(200));
        v.add(meta.furikomisaki3, v.maxlength(200));
        return v.validate();
    }
}
