package agri.service.kintai;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.kintai.DakokuMeta;
import agri.model.User;
import agri.model.kintai.Dakoku;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class DakokuService {
    private DakokuMeta meta = DakokuMeta.get();
    
    public Dakoku get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Dakoku get(Key userKey, Date date) {
        return Datastore.query(meta).filter(meta.userRef.equal(userKey), meta.date.equal(date)).asSingle();
    }
    
    public List<Dakoku> getList(User user, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.asc).asList();
    }
    
    public int getMinYear(User user) {
        Dakoku dakoku = Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey()))
                .sort(meta.date.asc).limit(1).asSingle();
        Calendar cal = Util.getCalendar();
        if(dakoku != null) {
            cal.setTime(dakoku.getDate());
        }
        return cal.get(Calendar.YEAR);
    }
    
    public int getMinYear() {
        Dakoku dakoku = Datastore.query(meta)
                .sort(meta.date.asc).limit(1).asSingle();
        Calendar cal = Util.getCalendar();
        if(dakoku != null) {
            cal.setTime(dakoku.getDate());
        }
        return cal.get(Calendar.YEAR);
    }

    public void insert(Dakoku dakoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, dakoku);
        tx.commit();
    }

    public void update(Dakoku dakoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, dakoku);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Dakoku dakoku = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, dakoku.getKey());
        tx.commit();
    }
}
