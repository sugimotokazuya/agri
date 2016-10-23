package agri.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.HatakeMeta;
import agri.model.Hatake;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class HatakeService {

    private HatakeMeta meta = HatakeMeta.get();
    
    public Hatake get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    //データ移行用
    public List<Hatake> getAllAll() {
        return Datastore.query(meta).sort(
            meta.area.desc, meta.name.asc).asList();
    }
    
    public List<Hatake> getAll(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey())).sort(
            meta.area.desc, meta.name.asc).asList();
    }

    public void insert(Hatake hatake) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hatake);
        tx.commit();
    }

    public Hatake update(Hatake hatake) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hatake);
        tx.commit();
        return hatake;
    }

    public void delete(Key key, Long version) {
        
        Transaction tx = Datastore.beginTransaction();
        Hatake hatake = get(key, version);
        if(hatake.getSakudukeListRef().getModelList().size() > 0
                || hatake.getSokuteiListRef().getModelList().size() > 0) {
            hatake.setDelete(true);
            Datastore.put(hatake);
        } else {
            Datastore.delete(key);
        }
        
        tx.commit();
    }
}
