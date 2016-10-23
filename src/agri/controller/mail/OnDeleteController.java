package agri.controller.mail;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.mail.OneNoticeMeta;
import agri.model.mail.OneNotice;
import agri.service.Util;
import agri.service.mail.OneNoticeService;

import com.google.appengine.api.datastore.Key;

public class OnDeleteController extends BaseController {

    private OneNoticeService service = new OneNoticeService();
    private OneNoticeMeta meta = OneNoticeMeta.get();
    
    @Override
    public boolean validateAuth() {
        OneNotice on = service.get(asKey(meta.key), asLong(meta.version));
        if(!on.getYagoRef().getModel().equals(loginYago)) {
            return false;
        }
        return loginUser.isAuthMailEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Key key = asKey(meta.key);
        Long version = asLong(meta.version);
        
        OneNotice on = service.get(key, version);
        Calendar cal = Util.getCalendar();
        cal.setTime(on.getDate());
        
        service.delete(on);
        
        return redirect(basePath + "notice/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1));
    }
}
