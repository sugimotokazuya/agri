package agri.controller.shinkoku.shiwake;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.ShiwakeService;
import agri.service.shinkoku.TekiyoShukeiService;

import com.google.appengine.api.datastore.Key;

public class ViewController extends BaseController {

    private KamokuService kamokuService = new KamokuService();
    private ShiwakeService service = new ShiwakeService();
    private TekiyoShukeiService tsService = new TekiyoShukeiService();
    
    @Override
    public boolean validateAuth() {
        if(!Util.isEmpty(asString("kamoku"))) {
            Kamoku kamoku = kamokuService.get(asKey("kamoku"));
            if(!loginYago.equals(kamoku.getYagoRef().getModel())) return false;
        }
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {

        if(asInteger("year") != null) {
            sessionScope("shiwakeViewYear", asInteger("year"));
            if(!Util.isEmpty(asString("kamoku"))) {
                sessionScope("shiwakeViewKamoku", asKey("kamoku"));
            } else {
                sessionScope("shiwakeViewKamoku", null);
            }
        }
        
        tsService.shukei(loginYago, (int)sessionScope("shiwakeViewYear"));
        
        int year = (Integer)sessionScope("shiwakeViewYear");
        
        List<Shiwake> shiwakeList = 
                service.getByNendo(loginYago,  year);
        
        requestScope("shiwakeList", shiwakeList);
        
        if(sessionScope("shiwakeViewKamoku") == null)
        {
            if(request.getAttribute("print") == null) {
                return forward("view.jsp");
            } else {
                return forward("viewPrint.jsp");
            }
        }
        else
        {
            Kamoku kamoku = kamokuService.get(
                (Key)sessionScope("shiwakeViewKamoku"));
            requestScope("kamoku", kamoku);
            if(request.getAttribute("print") == null) {
                return forward("viewKamoku.jsp");
            } else {
                return forward("viewKamokuPrint.jsp");
            }
        }
    }
}
