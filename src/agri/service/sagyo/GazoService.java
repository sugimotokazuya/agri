package agri.service.sagyo;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.GazoMeta;
import agri.model.User;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.Sagyo;
import agri.service.Const;
import agri.service.ImageUtil;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.tools.cloudstorage.GcsFilename;

public class GazoService {

    private GazoMeta meta = GazoMeta.get();
    
    public Gazo get(Key key) {
        return Datastore.get(meta,key);
    }
    
    public List<Gazo> getByUser(User user) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.use.equal(false))
                .sort(meta.date.desc)
                .asList();
    }
    
    public List<Gazo> getBySagyo(Sagyo sagyo) {
        return Datastore.query(meta)
                .filter(meta.sagyoRef.equal(sagyo.getKey())
                    , meta.use.equal(true))
                .sort(meta.date.desc)
                .asList();
    }

    public void insert(String contentType, byte[] bytes, Gazo gazo) throws Exception {
        
        Blob thumb = ImageUtil.createThumbnail(bytes, 300);
        gazo.setContentType(contentType);
        gazo.setThumb300(thumb);
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, gazo);
        String fileName = KeyFactory.keyToString(gazo.getKey());
        ImageUtil.putImage(fileName, contentType, bytes);
        tx.commit();
    }
    
    public void delete(Key key) {
        
        String keyStr = KeyFactory.keyToString(key);
        GcsFilename fileName = new GcsFilename(Const.APP_NAME, keyStr);
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey =
                blobstoreService.createGsBlobKey("/gs/" + fileName.getBucketName() + "/"
                        + fileName.getObjectName());
        
        blobstoreService.delete(blobKey);
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, key);
        tx.commit();
    }
    
    public void update(Gazo gazo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, gazo);
        tx.commit();
    }
}
