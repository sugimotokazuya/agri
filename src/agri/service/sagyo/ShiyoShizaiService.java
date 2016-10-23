package agri.service.sagyo;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.ShiyoShizaiMeta;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.ShiyoShizai;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShiyoShizaiService {
    
    private ShiyoShizaiMeta meta = ShiyoShizaiMeta.get();
    
    public void insert(ShiyoShizai shizai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shizai);
        tx.commit();
    }
    
    public List<ShiyoShizai> getListBySagyo(Sagyo sagyo) {
        
        return Datastore.query(meta).filter(meta.sagyoRef.equal(
            sagyo.getKey())).sort(meta.amount.asc).asList();
        
    }
    
    public void delete(Key key) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(key);
        tx.commit();
    }
}
