package agri.service.kintai;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.kintai.ShiftMeta;
import agri.model.User;
import agri.model.Yago;
import agri.model.kintai.Shift;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShiftService {
    private ShiftMeta meta = ShiftMeta.get();
    
    public Shift get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Shift get(User user, Date date) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.date.equal(date)).asSingle();
    }
    
    public List<Shift> getList(Yago yago, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.asc).asList();
    }
    
    public int getCount(User user, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end)).count();
    }

    public void insert(Shift shift) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shift);
        tx.commit();
    }

    public void update(Shift shift) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shift);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Shift shift = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, shift.getKey());
        tx.commit();
    }
}
