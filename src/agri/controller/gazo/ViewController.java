package agri.controller.gazo;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.GazoMeta;
import agri.model.sagyo.Gazo;
import agri.service.Const;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;

public class ViewController extends BaseController {

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
        String keyStr = KeyFactory.keyToString(gazo.getKey());
        
        GcsFilename fileName = new GcsFilename(Const.BUCKET_NAME, keyStr);

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey =
                blobstoreService.createGsBlobKey("/gs/" + fileName.getBucketName() + "/"
                        + fileName.getObjectName());
        
        response.setContentType("image/jpeg");
        
        blobstoreService.serve(blobKey, response);
        
        return null;
    }
    
}
