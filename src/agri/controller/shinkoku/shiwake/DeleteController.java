package agri.controller.shinkoku.shiwake;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.shinkoku.ShiwakeMeta;
import agri.model.shinkoku.Shiwake;
import agri.service.Util;
import agri.service.shinkoku.ShiwakeService;
import agri.service.shinkoku.TekiyoShukeiService;

public class DeleteController extends BaseController {

    private ShiwakeService service = new ShiwakeService();
    private TekiyoShukeiService shukeiService = new TekiyoShukeiService();
    private ShiwakeMeta meta = ShiwakeMeta.get();
    
    @Override
    public boolean validateAuth() {
        Shiwake shiwake = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(shiwake.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        Shiwake shiwake = service.get(asKey(meta.key), asLong(meta.version));
        Calendar cal = Util.getCalendar();
        cal.setTime(shiwake.getHiduke());
        int year = service.getNendo(loginYago, cal);
        
        shukeiService.mainasuShukei(year, shiwake);
        service.delete(shiwake);
        return redirect(basePath + "view");
        //return redirect(basePath);
    }
}
