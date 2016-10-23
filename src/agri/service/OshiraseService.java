package agri.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.OshiraseMeta;
import agri.model.Oshirase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class OshiraseService {

    private OshiraseMeta meta = OshiraseMeta.get();
    
    public Oshirase get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public Oshirase get(Key key) {
        return Datastore.get(meta, key);
    }
    
    public List<Oshirase> getAll() {
        return Datastore.query(meta).sort(meta.date.desc).asList();
    }
    
    public List<Oshirase> getListLimit5() {
        return Datastore.query(meta).sort(meta.date.desc).limit(5).asList();
    }

    public void insert(Oshirase oshirase) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, oshirase);
        tx.commit();
    }
    
    public void update(Oshirase oshirase) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, oshirase);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Oshirase oshirase = get(key, version);
        Datastore.delete(oshirase.getKey());
        tx.commit();
    }
}
