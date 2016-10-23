package agri.controller.sehi.sokutei;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sehi.SokuteiMeta;
import agri.model.Hatake;
import agri.model.sehi.Sokutei;
import agri.service.HatakeService;
import agri.service.Util;
import agri.service.sehi.SokuteiService;

import com.google.appengine.api.datastore.KeyFactory;


public class EditController extends BaseController {

    private SokuteiMeta meta = SokuteiMeta.get();
    private SokuteiService service = new SokuteiService();
    private HatakeService hatakeService = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        Sokutei sokutei = service.get(asKey(meta.key), asLong(meta.version));
        if(!sokutei.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Sokutei sokutei = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(sokutei, request);
        Calendar cal = Util.getCalendar();
        cal.setTime(sokutei.getDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", (cal.get(Calendar.MONTH) + 1));
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        List<Hatake> hatakeList = hatakeService.getAll(loginYago);
        requestScope("hatakeList", hatakeList);
        requestScope("hatake", KeyFactory.keyToString(
            sokutei.getHatakeRef().getModel().getKey()));
        return forward("edit.jsp");
    }
}
