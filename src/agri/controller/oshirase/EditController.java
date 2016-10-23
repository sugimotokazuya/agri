package agri.controller.oshirase;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.OshiraseMeta;
import agri.model.Oshirase;
import agri.service.OshiraseService;
import agri.service.Util;


public class EditController extends BaseController {

    private OshiraseMeta meta = OshiraseMeta.get();
    private OshiraseService service = new OshiraseService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Oshirase oshirase = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(oshirase, request);
        
        Calendar cal = Util.getCalendar();
        cal.setTime(oshirase.getDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        return forward("edit.jsp");
    }
}
