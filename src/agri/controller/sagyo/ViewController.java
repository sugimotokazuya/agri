package agri.controller.sagyo;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.cloud.sql.jdbc.internal.Util;

public class ViewController extends BaseController {

    private SakudukeService sakudukeService = new SakudukeService();
    private SakudukeMeta meta = SakudukeMeta.get();
    private SagyoItemService siService = new SagyoItemService();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = sakudukeService.get(asKey(meta.key));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Sakuduke sakuduke = sakudukeService.get(asKey(meta.key));
        requestScope("sakuduke", sakuduke);
        
        List<SagyoItem> siList = siService.getAll(loginYago);
        requestScope("siList", siList);
        if(!Util.isEmpty(asString("sagyoItem"))) {
            Key siKey = KeyFactory.stringToKey(asString("sagyoItem"));
            requestScope("siKey", siKey);
            requestScope("si", siService.get(siKey));
        }
        
        return forward("view.jsp");
    }
}
