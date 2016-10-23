package agri.cool;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthFilter implements Filter {
    
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        final HttpServletRequest req = (HttpServletRequest) request;  
        final UserService userService = UserServiceFactory.getUserService();  
      
        // リクエストのあったURL  
        final String requestUri = req.getRequestURI();
      
        // googleアカウントにログインしているかを判定するUtilクラス(後述)  
        if (isGoogleLogin(req) == false) {
            HttpSession session = req.getSession(true);
            session.invalidate();
          if(userService.getCurrentUser() != null) {
              ((HttpServletResponse) response).sendRedirect(userService.createLogoutURL(requestUri));
          } else {
              ((HttpServletResponse) response).sendRedirect(userService.createLoginURL(requestUri));
          }
          return;  
        }  

//        HttpSession session = req.getSession(true);
//        Key userKey = null;
//        if(session.getAttribute(Const.SES_USER) != null) {
//            Object obj = session.getAttribute(Const.SES_USER);
//            if( obj instanceof BytesHolder) {
//                BytesHolder bh = (BytesHolder) obj;
//                String str = new String(bh.getBytes());
//                userKey = str;
//            } else {
//                userKey = (Key) obj;
//            }
//        }
//        if(userKey == null) {
//            userKey = service.getByEmail(userService.getCurrentUser().getEmail()).getKey();
//        }
//        session.setAttribute(Const.SES_USER, userKey);
//        
      
        // ログイン成功  
        chain.doFilter(request, response); 
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    
    private static boolean isGoogleLogin(final HttpServletRequest request) {  
        
        final UserService userService = UserServiceFactory.getUserService();  
        final Principal principal = request.getUserPrincipal();
        if (principal == null || userService.isUserLoggedIn() == false) {
          return false;  
        } 
        String uri = request.getRequestURI();
        if(uri.startsWith("/shinkoku/")
                && !userService.getCurrentUser().getEmail().equals("sugimotokazuya@gmail.com")
                && !userService.getCurrentUser().getEmail().equals("kazuya@haruhibatake.jp"))
        {
            return false;
        } else if(uri.startsWith("/yago/")
                && !userService.getCurrentUser().getEmail().equals("sugimotokazuya@gmail.com"))
        {
            return false;
        } else if(uri.startsWith("/oshirase/")
                && !userService.getCurrentUser().getEmail().equals("sugimotokazuya@gmail.com"))
        {
            return false;
        }
        
        return true;  
      }  

}
