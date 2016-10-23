package agri.controller.kintai;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.kintai.Dakoku;
import agri.service.Util;
import agri.service.kintai.DakokuService;

import com.google.appengine.api.datastore.Key;

public class DakokuTeiseiController extends BaseController {

    private DakokuService dakokuService = new DakokuService();

    @Override
    public Navigation run() throws Exception {
        
        Calendar today = Util.getCalendar();
        Util.resetTime(today);
        
        Key userKey = asKey("key");
        Dakoku dakoku = dakokuService.get(userKey, today.getTime());
        
        int status = asInteger("status");
        switch(status) {
        case 0:
            dakokuService.delete(dakoku.getKey(), dakoku.getVersion());
            break;
        case 1:
            dakoku.setEnd(null);
            dakokuService.update(dakoku);
            break;
        case 3:
            dakoku.setOut(null);
            dakokuService.update(dakoku);
            break;
        case 4:
            dakoku.setIn(null);
            dakokuService.update(dakoku);
            break;
        case 5:
            dakoku.setEnd(null);
            dakokuService.update(dakoku);
            break;
        }

        return redirect("/kintai/dakoku");
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.equals(loginYago.getKintaiRef().getModel())
                || loginUser.isAuthKintai();
    }
}
