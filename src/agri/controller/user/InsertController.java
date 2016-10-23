package agri.controller.user;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.UserMeta;
import agri.model.User;
import agri.service.UserService;

import com.google.appengine.api.datastore.Email;

public class InsertController extends BaseController {

    private UserMeta meta = UserMeta.get();
    private UserService service = new UserService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthUserEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("create");
        }
        
        // 既存のユーザーチェック
        User checkUser = service.getByEmail(asString("email"));
        if(checkUser != null) {
            requestScope("alreadyExistUser", true);
            return forward("create");
        }
        checkUser = service.getByEmail2(asString("email2Str"));
        if(checkUser != null) {
            requestScope("alreadyExistUser", true);
            return forward("edit.jsp");
        }
        
        User user = new User();
        user.setName(asString("name"));
        user.setEmployeeType(asInteger("employeeType"));
        user.setEmail(new Email(asString("email")));
        user.setEmail2(new Email(asString("email2")));
        user.getYagoRef().setModel(loginYago);
        service.insert(user);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.email, v.required(), v.maxlength(100));
        v.add(meta.email2, v.maxlength(100));
        v.add(meta.employeeType, v.maxlength(1), v.integerType(), v.required());
        return v.validate();
    }
}
