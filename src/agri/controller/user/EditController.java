package agri.controller.user;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.UserMeta;
import agri.model.User;
import agri.service.UserService;


public class EditController extends BaseController {

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
        
        User user = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(user, request);
        requestScope("emailStr", user.getEmailStr());
        requestScope("email2Str", user.getEmail2Str());
        return forward("edit.jsp");
    }
}
