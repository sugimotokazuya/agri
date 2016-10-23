package agri.service.kintai;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.kintai.KinmuhyoMeta;
import agri.model.User;
import agri.model.Yago;
import agri.model.kintai.Kinmuhyo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class KinmuhyoService {
    private KinmuhyoMeta meta = KinmuhyoMeta.get();

    
    public Kinmuhyo get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Kinmuhyo get(Key key) {
        return Datastore.get(meta, key);
    }
    
    public Kinmuhyo get(User user, Date date) {
        return Datastore.query(meta).filter(
            meta.userRef.equal(user.getKey())
            , meta.date.equal(date)).asSingle();
    }
   
    public List<Kinmuhyo> getList(Yago yago, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.desc).asList();
    }
    
    public List<Kinmuhyo> getList(User user, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.desc).asList();
    }

    public void insert(Kinmuhyo kinmuhyo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kinmuhyo);
        tx.commit();
    }

    public void update(Kinmuhyo kinmuhyo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kinmuhyo);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Kinmuhyo kinmuhyo = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, kinmuhyo.getKey());
        tx.commit();
    }
}
