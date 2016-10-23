package agri.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.YagoMeta;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class YagoService {

    private YagoMeta meta = YagoMeta.get();
    
    public Yago get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public Yago get(Key key) {
        return Datastore.get(meta, key);
    }
    
    // InitController用
    public Yago getByName(String name) {
        return Datastore.query(meta).filter(meta.name.equal(name)).asSingle();
    }
    
    public List<Yago> getAll() {
        return Datastore.query(meta).filter(meta.delete.equal(false)).sort(
            meta.name.asc).asList();
    }
    
    public List<Yago> getAllWithDeleted() {
        return Datastore.query(meta).sort(
            meta.name.asc).asList();
    }

    public void insert(Yago yago) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, yago);
        tx.commit();
    }
    
    public void update(Yago yago) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, yago);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Yago yago = get(key, version);
        if(yago.getUserListRef().getModelList().size() > 0) {
            yago.setDelete(true);
            Datastore.put(yago);
        } else {
            Datastore.delete(yago.getKey());
        }
        tx.commit();
    }
}
