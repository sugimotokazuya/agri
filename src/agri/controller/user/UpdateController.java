package agri.controller.user;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.UserMeta;
import agri.model.User;
import agri.service.UserService;

import com.google.appengine.api.datastore.Email;

public class UpdateController extends BaseController {

    private UserMeta meta = UserMeta.get();
    private UserService service = new UserService();
    
    @Override
    public boolean validateAuth() {
        User user = service.get(asKey(meta.key), asLong(meta.version));
        if(!user.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthUserEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        
        User user = service.get(asKey(meta.key), asLong(meta.version));
        
        // 既存のユーザーチェック
        User checkUser = service.getByEmail(asString("emailStr"));
        if(checkUser != null && !user.equals(checkUser)) {
            requestScope("alreadyExistUser", true);
            return forward("edit.jsp");
        }
        checkUser = service.getByEmail2(asString("email2Str"));
        if(checkUser != null && !user.equals(checkUser)) {
            requestScope("alreadyExistUser", true);
            return forward("edit.jsp");
        }
        
        BeanUtil.copy(request, user);
        user.setEmail(new Email(asString("emailStr")));
        user.setEmail2(new Email(asString("email2Str")));
        service.update(user);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add("emailStr", v.required(), v.maxlength(100));
        v.add("email2Str", v.maxlength(100));
        v.add(meta.employeeType, v.maxlength(1), v.integerType());
        return v.validate();
    }
}
