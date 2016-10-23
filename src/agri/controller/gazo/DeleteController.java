package agri.controller.gazo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.GazoMeta;
import agri.model.sagyo.Gazo;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.datastore.Key;

public class DeleteController extends BaseController {

    private GazoService gazoService = new GazoService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
      
        if(!loginUser.isAuthSagyoEdit()) return null;
        
        Key key = asKey(GazoMeta.get().key);
        Gazo gazo = gazoService.get(key);
        if(gazo == null) return null;

        gazoService.delete(key);
        
        return null;
    }
    
}
