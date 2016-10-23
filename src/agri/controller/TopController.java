package agri.controller;

import java.security.Principal;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import agri.model.User;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TopController extends Controller {

    private agri.service.UserService userService = new agri.service.UserService();
    
    @Override
    public Navigation run() throws Exception {
        
        UserService us = UserServiceFactory.getUserService();  
        Principal principal = request.getUserPrincipal();  
        if (principal == null || us.isUserLoggedIn() == false) {  
            return forward("top.jsp");  
        } else {
            String email = us.getCurrentUser().getEmail();
            User loginUser = userService.getByEmail(email);
            if(loginUser == null) {
                return forward("errorUser.jsp");
            } else {
                return redirect("start");
            }
        }
    }
 
}
