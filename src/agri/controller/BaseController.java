package agri.controller;

import java.security.Principal;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import agri.model.User;
import agri.model.Yago;
import agri.model.mail.OneNotice;
import agri.service.Const;
import agri.service.OshiraseService;
import agri.service.UserService;
import agri.service.Util;
import agri.service.YagoService;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class BaseController extends Controller {

    protected UserService userService = new UserService();
    private OshiraseService oService = new OshiraseService();
    private OneNoticeService onService = new OneNoticeService();
    private YagoService yagoService = new YagoService();
    protected Yago loginYago;
    protected User loginUser;

    protected Navigation setUp() {
        
        com.google.appengine.api.users.UserService us = UserServiceFactory.getUserService();
        final Principal principal = request.getUserPrincipal(); 
        if (principal == null || us.isUserLoggedIn() == false) {  
            return redirect(basePath);  
        } 
        String email = us.getCurrentUser().getEmail();
        loginUser = userService.getByEmail(email);
        if(loginUser == null) {
            if(!email.equals("sugimotokazuya@gmail.com")) return redirect("/top");
            // データベース初期化後の初期データのセット
            Yago yago = new Yago();
            yago.setName("はるひ畑");
            yagoService.insert(yago);
            User user = new User();
            user.getYagoRef().setModel(yago);
            user.setName("杉本 和也");
            user.setEmail(new Email("sugimotokazuya@gmail.com"));
            user.setAuthUserView(true);
            user.setAuthUserEdit(true);
            userService.insert(user);
            loginUser = user;
            yago.getOwnerRef().setModel(loginUser);
            yagoService.update(yago);
        }
        if(sessionScope("superYago") != null 
                && loginUser.getEmailStr().equals("sugimotokazuya@gmail.com")) {
            this.loginYago = sessionScope("superYago");
        } else {
            this.loginYago = loginUser.getYagoRef().getModel();
        }
        sessionScope(Const.SES_USER_NAME, loginUser.getName());
        sessionScope(Const.SES_YAGO_NAME, loginUser.getYagoRef().getModel().getName());
        requestScope("loginUser", loginUser);
        
        requestScope("oList", oService.getListLimit5());
        
        if(loginUser.isAuthMailView()) {
            List<OneNotice> onList = onService.getListByLast5(loginYago);
            requestScope("onList", onList);
        }
        
        requestScope("today", Util.getCalendar().getTime());
        boolean isOwner = loginYago.getOwnerRef().getModel().equals(loginUser);
        requestScope("isOwner", isOwner);
        
        
        if(validateAuth()) {
            return null;
        } else {
            return redirect("/");
        }
        
    }
    
    protected String asString(CharSequence name) throws NullPointerException {
        String str = super.asString(name);
        return Util.trim(str);
    }
    
    protected abstract boolean validateAuth();
 
}
