package agri.controller;

import java.security.Principal;
import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.model.User;
import agri.service.Util;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class StartController extends BaseController {
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        UserService us = UserServiceFactory.getUserService();  
        Principal principal = request.getUserPrincipal();  
        if (principal == null || us.isUserLoggedIn() == false)
            return redirect("top");  

        Calendar cal = Util.getCalendar();
        sessionScope("year", cal.get(Calendar.YEAR));
        sessionScope("month", cal.get(Calendar.MONTH));
        sessionScope("torihikisakiRef", "");
        
        String email = us.getCurrentUser().getEmail();
        User loginUser = userService.getByEmail(email);
        if(loginUser == null) return forward("errorUser.jsp");
        
        return forward("start.jsp");
    }
 
}
