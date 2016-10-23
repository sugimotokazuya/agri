package agri.controller.shinkoku.shiwake;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.ShiwakeService;
import agri.service.shinkoku.TekiyoShukeiService;

public class InsertController extends BaseController {

    private ShiwakeService service = new ShiwakeService();
    private TekiyoShukeiService shukeiService = new TekiyoShukeiService();
    private KamokuService kamokuService = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        Kamoku kariKamoku = kamokuService.get(asKey("karikataKamoku"));
        if(!loginYago.equals(kariKamoku.getYagoRef().getModel())) return false;
        Kamoku kashiKamoku = kamokuService.get(asKey("kashikataKamoku"));
        if(!loginYago.equals(kashiKamoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        
        sessionScope("shiwakeYear", asString("year"));
        sessionScope("shiwakeMonth", asString("month"));
        sessionScope("shiwakeDay", asString("day"));
        
        
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Shiwake shiwake = new Shiwake();
        shiwake.setHiduke(cal.getTime());
        shiwake.getKarikataKamokuRef().setKey(asKey("karikataKamoku"));
        shiwake.setKarikataKingaku(asInteger("kingaku"));
        shiwake.getKashikataKamokuRef().setKey(asKey("kashikataKamoku"));
        shiwake.setKashikataKingaku(asInteger("kingaku"));
        shiwake.getTekiyoRef().setKey(asKey("tekiyo"));
        shiwake.setTekiyoText(asString("tekiyoText"));
        shiwake.getYagoRef().setModel(loginYago);
        shiwake.setNendo(service.getNendo(loginYago, cal));
        service.insert(shiwake);
        shukeiService.shukei(loginYago, shiwake.getNendo());
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("year", v.required(), 
            v.longRange(2011, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add("kingaku", v.required(), v.longRange(0, 99999999));
        v.add("tekiyoText", v.maxlength(20));
        return v.validate();
    }
}
