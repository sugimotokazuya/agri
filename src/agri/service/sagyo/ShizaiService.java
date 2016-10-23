package agri.service.sagyo;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.ShizaiMeta;
import agri.model.sagyo.Shizai;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShizaiService {

    private ShizaiMeta meta = ShizaiMeta.get();
    
    public Shizai get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }

    public void insert(Shizai shizai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shizai);
        tx.commit();
    }
    
    public void update(Shizai shizai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shizai);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        
        Shizai shizai = get(key, version);
        if(shizai.getShiyoShizaiListRef().getModelList().size() > 0) {
            shizai.setDelete(true);
            Datastore.put(shizai);
        } else {
            Datastore.delete(key);
        }
        tx.commit();
    }
}
