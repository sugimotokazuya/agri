package agri.controller.gazo;

import javax.servlet.ServletOutputStream;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.GazoMeta;
import agri.model.sagyo.Gazo;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.datastore.Key;

public class ViewThumbController extends BaseController {

    private GazoService gazoService = new GazoService();
    
    @Override
    public boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if(!loginUser.isAuthSakudukeView()) return null;
        Key key = asKey(GazoMeta.get().key);
        Gazo gazo = gazoService.get(key);
        if(gazo == null) return null;
        
        response.setContentType(gazo.getContentType()); 
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            byte[] bytes = gazo.getThumb300().getBytes();
            out.write(bytes, 0, bytes.length);
        } finally {
            if ( out != null ) out.close();
        }
        
        return null;
    }
    
}
