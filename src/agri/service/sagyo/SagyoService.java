package agri.service.sagyo;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.SagyoMeta;
import agri.model.Yago;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SagyoService {

    private SagyoMeta meta = SagyoMeta.get();
    
    public List<Sagyo> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public Sagyo get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public Sagyo get(Key key) {
        return Datastore.get(meta,key);
    }
  
    public List<Sagyo> getNew5BySakuduke(Sakuduke sakuduke) {
        return Datastore.query(meta).filter(meta.sakudukeRef.equal(sakuduke.getKey())).sort(
            meta.date.desc).limit(5).asList();
    }
    
    public List<Sagyo> getByDateRange(Yago yago, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end)).sort(meta.date.asc).asList();
    }
    
    public List<Sagyo> getByDateRangeSagyoItem(Yago yago, Date start, Date end, SagyoItem si) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.sagyoItemRef.equal(si.getKey())
                    , meta.date.lessThan(end)).sort(meta.date.asc).asList();
    }
    
    public Key insert(Sagyo sagyo) {
        Transaction tx = Datastore.beginTransaction();
        Key key = Datastore.put(tx, sagyo);
        tx.commit();
        return key;
    }
    
    public void update(Sagyo sagyo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sagyo);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(key);
        tx.commit();
    }
}
