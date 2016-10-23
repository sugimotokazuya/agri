package agri.controller.sagyo;

import java.text.SimpleDateFormat;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.Sakuduke;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CsvController extends BaseController {

    private SakudukeService sakudukeService = new SakudukeService();
    private SakudukeMeta meta = SakudukeMeta.get();
    
    @Override
    public boolean validateAuth() {
        Sakuduke sakuduke = sakudukeService.get(asKey(meta.key));
        if(!sakuduke.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Sakuduke sakuduke = sakudukeService.get(asKey(meta.key));

        if(!Util.isEmpty(asString("sagyoItem"))) {
            Key siKey = KeyFactory.stringToKey(asString("sagyoItem"));
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat sdf = Util.getSimpleDateFormat("yyyy/MM/dd");
            for(Sagyo sagyo : sakuduke.getSagyoListRef().getModelList()) {
                if(!sagyo.getSagyoItemRef().getKey().equals(siKey)) continue;
                sb.append(sdf.format(sagyo.getDate()));
                sb.append(",");
                sb.append(sagyo.getAmount());
                sb.append("\r\n");
            }
            byte[] arr = sb.toString().getBytes();
            download("sagyo.csv", arr);
        }
        
        return null;
    }
}
