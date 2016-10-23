package agri.service.hanbai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.ShukkaCountMeta;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaCount;
import agri.model.hanbai.ShukkaKingaku;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShukkaCountService {
    
    private ShukkaCountMeta meta = ShukkaCountMeta.get();
    private ShukkaKingakuService skService = new ShukkaKingakuService(this);
    
    public void insert(ShukkaCount shukkaCount) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shukkaCount);
        tx.commit();
    }
    
    public List<ShukkaCount> getListByShukkaKingaku(ShukkaKingaku sk) {
        List<ShukkaCount> list = Datastore.query(meta).filter(
            meta.shukkaKingakuRef.equal(sk.getKey())).asList();
        return list;
    }
    
    public void deleteByShukkaKingaku(Key skKey) {
        Transaction tx;
        Iterator<Key> keys = Datastore.query(meta).filter(
            meta.shukkaKingakuRef.equal(skKey)).asKeyIterator();
        while(keys.hasNext()) {
            tx = Datastore.beginTransaction();
            Datastore.delete(tx, keys.next());
            tx.commit();
        }
    }
    
    public void delete(Key key) {
        Transaction tx;
        tx = Datastore.beginTransaction();
        Datastore.delete(tx, key);
        tx.commit();
    }
    
    public List<ShukkaCount> getListByShukka(Shukka shukka) {
        List<ShukkaCount> list = new ArrayList<ShukkaCount>();
        List<ShukkaKingaku> skList = skService.getListByShukka(shukka);
        Iterator<ShukkaKingaku> ite = skList.iterator();
        while(ite.hasNext()) {
            ShukkaKingaku sk = ite.next();
            list.addAll(getListByShukkaKingaku(sk));
        }
        return list;
    }
}
