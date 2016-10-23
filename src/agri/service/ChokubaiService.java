package agri.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.ChokubaiMeta;
import agri.model.Chokubai;
import agri.model.Torihikisaki;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ChokubaiService {

    private ChokubaiMeta meta = ChokubaiMeta.get();
    
    // TODO 初期化用
    public List<Chokubai> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public Chokubai get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Chokubai getByName(Torihikisaki t, String name) {
        Chokubai chokubai = Datastore.query(meta).filter(
            meta.torihikisakiRef.equal(t.getKey()),
            meta.delete.equal(false),
            meta.name.equal(name)).asSingle();
        return chokubai;
    }
    
    public List<Chokubai> getListByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta).filter(
            meta.torihikisakiRef.equal(t.getKey()),
            meta.delete.equal(false)).sort(meta.order.asc).asList();
    }
    
    public List<Chokubai> getListByTorihikisaki(Key key) {
        return Datastore.query(meta).filter(
            meta.torihikisakiRef.equal(key),
            meta.delete.equal(false)).sort(meta.order.asc).asList();
    }
    
    public List<Chokubai> getAllListByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta).filter(
            meta.torihikisakiRef.equal(t.getKey())).sort(meta.order.asc).asList();
    }
    
    public void insert(Chokubai chokubai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, chokubai);
        tx.commit();
    }
    
    public void update(Chokubai c) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, c);
        tx.commit();
    }
    
    public void delete(Chokubai c) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, c.getKey());
        tx.commit();
    }
    
    public void sort(List<Chokubai> cList) {
        Collections.sort(cList, new ChokubaiComparator());
    }
    
    public class ChokubaiComparator implements Comparator<Chokubai> {
        @Override
        public int compare(Chokubai a, Chokubai b) {
            
            int orderA = a.getOrder();
            int orderB = b.getOrder();
            
            if(orderA > orderB) {
                return 1;
            } else if(orderA == orderB) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
}
