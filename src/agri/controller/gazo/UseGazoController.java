package agri.controller.gazo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.Sagyo;
import agri.service.sagyo.GazoService;
import agri.service.sagyo.SagyoService;

import com.google.appengine.api.datastore.Key;

public class UseGazoController extends BaseController {

    private GazoService gazoService = new GazoService();
    private SagyoService sagyoService = new SagyoService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
      
        if(!loginUser.isAuthSagyoEdit()) return null;
        
        Key gazoKey = asKey("gazoKey");
        Gazo gazo = gazoService.get(gazoKey);
        if(gazo == null) return null;
        
        Key sagyoKey = asKey("sagyoKey");
        Sagyo sagyo = sagyoService.get(sagyoKey);
        if(sagyo == null) return null;
        
        gazo.getSagyoRef().setModel(sagyo);
        gazo.setUse(true);
        gazoService.update(gazo);
        
        return null;
    }
    
}
