package agri.service.shinkoku;

import org.slim3.datastore.Datastore;

import agri.meta.shinkoku.TekiyoMeta;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Tekiyo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;


public class TekiyoService {

    private TekiyoMeta meta = TekiyoMeta.get();
    
    public Tekiyo get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }

    public Tekiyo getByName(Kamoku kamoku, String name) {
        return Datastore.query(meta).filter(
            meta.kamokuRef.equal(kamoku.getKey()),
            meta.name.equal(name)).asSingle();
    }
    
    public void insert(Tekiyo tekiyo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, tekiyo);
        tx.commit();
    }

    public void update(Tekiyo tekiyo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, tekiyo);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Tekiyo tekiyo = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, tekiyo.getKey());
        tx.commit();
    }
}
