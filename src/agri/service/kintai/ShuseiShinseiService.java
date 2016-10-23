package agri.service.kintai;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.kintai.ShuseiShinseiMeta;
import agri.model.User;
import agri.model.Yago;
import agri.model.kintai.ShuseiShinsei;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShuseiShinseiService {
    private ShuseiShinseiMeta meta = ShuseiShinseiMeta.get();
    
    public int createStatus(String status) throws Exception {
        switch(status) {
        case "start": return 1;
        case "out": return 2;
        case "in": return 3;
        case "end": return 4;
        case "zangyo": return 5;
        }
        throw new Exception("Illegal Status[" + status + "]");
    }
    
    public ShuseiShinsei get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public ShuseiShinsei get(User user, Date date, int status) {
        return Datastore.query(meta).filter(
            meta.userRef.equal(user.getKey())
            , meta.date.equal(date)
            , meta.status.equal(status)
            , meta.shonin.equal(0)).asSingle();
    }
    
    public List<ShuseiShinsei> getList(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey())
            , meta.shonin.equal(0)).sort(meta.date.asc).asList();
    }
    
    public List<ShuseiShinsei> getShinseiChuList(User user, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.shonin.equal(0)
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.asc).asList();
    }
    
    public List<ShuseiShinsei> getList(User user, Date start, Date end) {
        return Datastore.query(meta)
                .filter(meta.userRef.equal(user.getKey())
                    , meta.date.greaterThanOrEqual(start)
                    , meta.date.lessThan(end))
                .sort(meta.date.asc).asList();
    }

    public void insert(ShuseiShinsei shuseiShinsei) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shuseiShinsei);
        tx.commit();
    }

    public void update(ShuseiShinsei shuseiShinsei) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shuseiShinsei);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        ShuseiShinsei shuseiShinsei = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, shuseiShinsei.getKey());
        tx.commit();
    }
}
