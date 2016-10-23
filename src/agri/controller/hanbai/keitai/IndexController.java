package agri.controller.hanbai.keitai;

import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;
import agri.service.hanbai.ShukkaKeitaiService;

public class IndexController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private String SES_TORIHIKISAKI = "/hanbai/keitai/torihikisakiRef";
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    @Override
    public Navigation run() throws Exception {
        
        requestScope("tList", tService.getAll(loginYago));

        Key tKey = null;
        if(asString("torihikisakiRef") != null) {
            tKey = "".equals(asString("torihikisakiRef")) ? null : asKey("torihikisakiRef");
            sessionScope(SES_TORIHIKISAKI, asString("torihikisakiRef"));
        } else {
            
            tKey = "".equals(sessionScope(SES_TORIHIKISAKI)) ||
                    sessionScope(SES_TORIHIKISAKI) == null
                    ? null : KeyFactory.stringToKey((String)sessionScope(SES_TORIHIKISAKI));
        }
        
        if(tKey==null) return forward("index.jsp");
        
        Torihikisaki t = tService.get(tKey);
        if(!loginYago.equals(t.getYagoRef().getModel())) {
            return redirect(basePath);
        }
        
        requestScope("torihikisakiRef", sessionScope(SES_TORIHIKISAKI));
        requestScope("shukkaKeitaiList", service.getByTorihikisaki(t));
        return forward("index.jsp");
    }
}
