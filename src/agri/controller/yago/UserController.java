package agri.controller.yago;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.YagoMeta;
import agri.model.User;
import agri.model.Yago;
import agri.service.UserService;
import agri.service.YagoService;

public class UserController extends BaseController {

    private YagoService service = new YagoService();
    private UserService userService = new UserService();
    private YagoMeta meta = YagoMeta.get();
    @Override
    public Navigation run() throws Exception {
        Yago yago = service.get(asKey(meta.key), asLong(meta.version));
        List<User> list = userService.getAll(yago);
        requestScope("yago", yago);
        requestScope("list", list);
        return forward("user.jsp");
    }
    
    @Override
    protected boolean validateAuth() {
    
        return true;
    }
}
