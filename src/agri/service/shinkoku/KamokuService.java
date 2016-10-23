package agri.service.shinkoku;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.shinkoku.KamokuMeta;
import agri.model.Yago;
import agri.model.shinkoku.Kamoku;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;


public class KamokuService {

    private KamokuMeta meta = KamokuMeta.get();
    
    public Kamoku get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Kamoku get(Key key) {
        return Datastore.get(meta, key);
    }

    public List<Kamoku> getAll(Yago yago) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey()))
                .sort(
            meta.kubun.asc, meta.name.asc).asList();
    }
    
    // 初期化用
    public List<Kamoku> getAllAll() {
        return Datastore.query(meta).sort(
            meta.kubun.asc, meta.name.asc).asList();
    }
    
    public List<Kamoku> getByKubun(Yago yago, int kubun) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.kubun.equal(kubun)).sort(meta.name.asc).asList();
    }

    public void insert(Kamoku kamoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kamoku);
        tx.commit();
    }

    public void update(Kamoku kamoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kamoku);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Kamoku kamoku = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, kamoku.getKey());
        tx.commit();
    }
}
