package agri.service.sagyo;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.SagyoItemMeta;
import agri.model.Yago;
import agri.model.sagyo.SagyoItem;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SagyoItemService {

    private SagyoItemMeta meta = SagyoItemMeta.get();
    
    public SagyoItem get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public SagyoItem get(Key key) {
        return Datastore.get(meta,key);
    }
    
    public List<SagyoItem> getAll(Yago yago) {
        return Datastore.query(meta).filter(
            meta.delete.equal(false), meta.yagoRef.equal(yago.getKey())).sort(
            meta.name.asc).asList();
    }
    
    // TODO Init用
    public List<SagyoItem> getAllAll() {
        return Datastore.query(meta).asList();
    }

    public void insert(SagyoItem workItem) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, workItem);
        tx.commit();
    }
    
    public void update(SagyoItem workItem) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, workItem);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        SagyoItem sagyoItem = get(key, version);
        if(sagyoItem.getSagyoListRef().getModelList().size() > 0) {
            sagyoItem.setDelete(true);
            Datastore.put(sagyoItem);
        } else {
            Datastore.delete(key);
        }
        tx.commit();
    }
}
