package agri.controller.hanbai.kakuduke;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.hanbai.KakudukeMeta;
import agri.model.hanbai.Kakuduke;
import agri.service.hanbai.KakudukeService;

public class DeleteController extends BaseController {

    private KakudukeService service = new KakudukeService();
    private KakudukeMeta meta = KakudukeMeta.get();

    @Override
    public boolean validateAuth() {
        Kakuduke k = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(k.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Kakuduke k = service.get(asKey(meta.key), asLong(meta.version));
        if(k.getShukkaRef().getModel() != null
                || k.getShukkaRecRef().getModel() != null) {
            return redirect(basePath);
        }
            
        service.delete(k);
        
        return redirect(basePath);
    }
}
