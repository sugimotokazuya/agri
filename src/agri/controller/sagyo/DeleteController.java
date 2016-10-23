package agri.controller.sagyo;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoMeta;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.ShiyoKikai;
import agri.model.sagyo.ShiyoShizai;
import agri.service.sagyo.GazoService;
import agri.service.sagyo.SagyoService;
import agri.service.sagyo.SagyoUserService;
import agri.service.sagyo.ShiyoKikaiService;
import agri.service.sagyo.ShiyoShizaiService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteController extends BaseController {

    private SagyoService service = new SagyoService();
    private ShiyoShizaiService shiyoShizaiService = new ShiyoShizaiService();
    private ShiyoKikaiService shiyoKikaiService = new ShiyoKikaiService();
    private SagyoUserService sagyoUserService = new SagyoUserService();
    private GazoService gazoService = new GazoService();
    private SagyoMeta meta = SagyoMeta.get();
    
    @Override
    public boolean validateAuth() {
        Sagyo sagyo = service.get(asKey(meta.key), asLong(meta.version));
        if(!sagyo.getSakudukeRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Sagyo sagyo = service.get(asKey(meta.key), asLong(meta.version));
        
        List<ShiyoShizai> list = shiyoShizaiService.getListBySagyo(sagyo);
        for(ShiyoShizai ss : list) {
            shiyoShizaiService.delete(ss.getKey());
        }
        
        List<ShiyoKikai> skList = shiyoKikaiService.getListBySagyo(sagyo);
        for(ShiyoKikai sk : skList) {
            shiyoKikaiService.delete(sk.getKey());
        }
        
        List<SagyoUser> suList = sagyoUserService.getListBySagyo(sagyo);
        for(SagyoUser su : suList) {
            sagyoUserService.delete(su.getKey());
        }
        
        List<Gazo> gazoList = gazoService.getBySagyo(sagyo);
        for(Gazo gazo : gazoList) {
            gazoService.delete(gazo.getKey());
        }
        
        service.delete(sagyo.getKey(),sagyo.getVersion());
        
        int page = asInteger("page") == null ? 0 : asInteger("page");
        
        if(page == 0) {
            return redirect(basePath + "view/" + KeyFactory.keyToString(
                sagyo.getSakudukeRef().getKey()));
        } else {
            return redirect(basePath);
        }
        
    }
}
