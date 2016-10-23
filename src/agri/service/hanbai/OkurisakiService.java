package agri.service.hanbai;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.OkurisakiMeta;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.Okurisaki;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class OkurisakiService {

    private OkurisakiMeta meta = OkurisakiMeta.get();
    
    public List<Okurisaki> getByYago(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(
            yago.getKey())).sort(meta.name.asc).asList();
    }
    
    public List<Okurisaki> getByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta).filter(meta.torihikisakiRef.equal(t.getKey())).sort(meta.name.asc).asList();
    }
    
    public Okurisaki get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public Okurisaki get(Key key) {
        return Datastore.get(meta,key);
    }
    
    public Okurisaki getByName(Yago yago, String name) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(
            yago.getKey())).filter(meta.name.equal(name)).asSingle();
    }
    
    public void insert(Okurisaki okurisaki) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, okurisaki);
        tx.commit();
    }
    
    public void update(Okurisaki okurisaki) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, okurisaki);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();        
        Datastore.delete(key);
        tx.commit();
    }
}
