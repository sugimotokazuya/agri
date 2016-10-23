package agri.service.kintai;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.kintai.TeijiMeta;
import agri.model.Yago;
import agri.model.kintai.Teiji;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class TeijiService {
    private TeijiMeta meta = TeijiMeta.get();
    
    public Teiji get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public List<Teiji> getList(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()))
                .sort(meta.start.asc, meta.end.asc).asList();
    }
    
    public List<Teiji> getByDateRange(Yago yago, Date start, Date end) {
        return Datastore.query(meta).filter(meta.end.greaterThanOrEqual(start)
             , meta.yagoRef.equal(yago.getKey())).sort(meta.end.desc).asList();
    }

    public void insert(Teiji teiji) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, teiji);
        tx.commit();
    }

    public void update(Teiji teiji) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, teiji);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Teiji teiji = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, teiji.getKey());
        tx.commit();
    }
}
