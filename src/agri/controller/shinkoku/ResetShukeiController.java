package agri.controller.shinkoku;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.shinkoku.TekiyoShukeiService;

public class ResetShukeiController extends BaseController {

    private TekiyoShukeiService shukeiService = new TekiyoShukeiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        int year = asInteger("year");
        shukeiService.resetShukei(loginYago, year);

        return forward("resetShukei.jsp");
    }
}
