package agri.controller.shinkoku.shiwake;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;

public class CreateController extends BaseController {

    KamokuService service = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        Kamoku kariKamoku = service.get(asKey("karikataKamoku"));
        if(!loginYago.equals(kariKamoku.getYagoRef().getModel())) return false;
        Kamoku kashiKamoku = service.get(asKey("kashikataKamoku"));
        if(!loginYago.equals(kashiKamoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Kamoku karikata =  service.get(asKey("karikataKamoku"));
        Kamoku kashikata = service.get(asKey("kashikataKamoku"));
        sessionScope("karikataKamoku", asString("karikataKamoku"));
        sessionScope("kashikataKamoku", asString("kashikataKamoku"));
        
        if(sessionScope("shiwakeYear") != null) requestScope("year", sessionScope("shiwakeYear"));
        if(sessionScope("shiwakeMonth") != null) requestScope("month", sessionScope("shiwakeMonth"));
        if(sessionScope("shiwakeDay") != null) requestScope("day", sessionScope("shiwakeDay"));
        
        requestScope("karikataKamoku", karikata);
        requestScope("kashikataKamoku", kashikata);
        if(kashikata.getKubun() == 3 || kashikata.getKubun() == 4) {
            requestScope("tekiyoList", kashikata.getTekiyoListRef().getModelList());
        } else {
            requestScope("tekiyoList", karikata.getTekiyoListRef().getModelList());
        }
        if(asString("year") == null) {
            Calendar cal = Util.getCalendar();
            requestScope("year", cal.get(Calendar.YEAR));
            requestScope("month", cal.get(Calendar.MONTH) + 1);
        }
        return forward("create.jsp");
    }
}
