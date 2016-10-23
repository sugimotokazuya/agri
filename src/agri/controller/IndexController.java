package agri.controller;

import java.security.Principal;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class IndexController extends Controller {

       
    
    @Override
    public Navigation run() throws Exception {
        
        UserService userService = UserServiceFactory.getUserService();  
        Principal principal = request.getUserPrincipal();  
        if (principal == null || userService.isUserLoggedIn() == false) {  
            return redirect("top");  
        } else {
            return redirect("start");
        }
        
    }
 
}
