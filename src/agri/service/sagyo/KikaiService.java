package agri.service.sagyo;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.KikaiMeta;
import agri.model.sagyo.Kikai;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class KikaiService {

    private KikaiMeta meta = KikaiMeta.get();
    
    public Kikai get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public void insert(Kikai kikai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kikai);
        tx.commit();
    }
    
    public void update(Kikai kikai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kikai);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Kikai kikai = get(key, version);
        if(kikai.getShiyoKikaiListRef().getModelList().size() > 0) {
            kikai.setDelete(true);
            Datastore.put(kikai);
        } else {
            Datastore.delete(key);
        }
        tx.commit();
    }
}
