package agri.controller.chokubai;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.service.ChokubaiService;
import agri.service.TorihikisakiService;

import com.google.appengine.api.datastore.Key;

public class IndexController extends BaseController {

    private ChokubaiService service = new ChokubaiService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        Key key = asKey("torihikisakiRef");
        if(key != null) {
            Torihikisaki t = tService.get(key);
            if(!loginYago.equals(t.getYagoRef().getModel())) return false;
        }
        return loginUser.isAuthTorihikisakiView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Key tKey = null;
        if(asString("torihikisakiRef") != "") {
            tKey = asKey("torihikisakiRef");
        }
        
        List<Torihikisaki> tList = tService.getChokubai(loginYago);
        if(tList.size() == 0) {
            requestScope("errorStr", "区分が直売の【取引先】を登録してから直売店舗管理を行ってください。");
            return forward("/torihikisaki/index");
        }
        requestScope("tList", tList);
        
        if(tList.size() == 0) return forward("index.jsp");
        
        if(tKey == null) tKey = tList.get(0).getKey();
        
        requestScope("list", service.getListByTorihikisaki(tKey));
        
        return forward("index.jsp");
    }
}
