package agri.controller.gazo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.GazoMeta;
import agri.model.sagyo.Gazo;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.datastore.Key;

public class KaijoController extends BaseController {

    private GazoService gazoService = new GazoService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
      
        if(!loginUser.isAuthSagyoEdit()) return null;
        
        Key gazoKey = asKey(GazoMeta.get().key);
        Gazo gazo = gazoService.get(gazoKey);
        if(gazo == null) return null;
        
        gazo.getSagyoRef().setModel(null);
        gazo.setUse(false);
        gazoService.update(gazo);
        
        return null;
    }
    
}
