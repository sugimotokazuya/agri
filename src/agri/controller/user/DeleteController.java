package agri.controller.user;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.UserMeta;
import agri.model.User;
import agri.service.UserService;

public class DeleteController extends BaseController {

    private UserService service = new UserService();
    private UserMeta meta = UserMeta.get();
    
    @Override
    public boolean validateAuth() {
        User user = service.get(asKey(meta.key), asLong(meta.version));
        if(!user.getYagoRef().getModel().equals(loginYago)) return false;
        // オーナーは削除不可
        if(loginYago.getOwnerRef().getModel().equals(user)) return false;
        // 自身も削除不可
        if(loginUser.equals(user)) return false;
        
        return loginUser.isAuthUserEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
