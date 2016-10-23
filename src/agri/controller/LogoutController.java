package agri.controller;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LogoutController extends Controller {

       
    
    @Override
    public Navigation run() throws Exception {
        
        UserService service = UserServiceFactory.getUserService();
        String logoutUrl = service.createLogoutURL(basePath);
        HttpSession session = request.getSession(true);
        session.invalidate();
        return redirect(logoutUrl);
    }
 
}
