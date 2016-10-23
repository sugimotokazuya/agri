package agri.service.sagyo;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.ShiyoKikaiMeta;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.ShiyoKikai;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShiyoKikaiService {
    
    private ShiyoKikaiMeta meta = ShiyoKikaiMeta.get();
    
    public void insert(ShiyoKikai kikai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kikai);
        tx.commit();
    }
    
    public List<ShiyoKikai> getListBySagyo(Sagyo sagyo) {
        
        return Datastore.query(meta).filter(meta.sagyoRef.equal(
            sagyo.getKey())).sort(meta.amount.asc).asList();
        
    }
    
    public void delete(Key key) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(key);
        tx.commit();
    }
}
