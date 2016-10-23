package agri.service.hanbai;

import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.ShukkaKingakuMeta;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaKingaku;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShukkaKingakuService {

    private ShukkaKingakuMeta meta = ShukkaKingakuMeta.get();
    private ShukkaCountService scService;
    
    public ShukkaKingakuService() {
        scService = new ShukkaCountService();
    }
    
    public ShukkaKingakuService(ShukkaCountService service) {
        scService = service;
    }
    
    public void insert(ShukkaKingaku shukkaKingaku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shukkaKingaku);
        tx.commit();
    }
    
    public List<ShukkaKingaku> getListByShukka(Shukka s) {
        List<ShukkaKingaku> list = Datastore.query(meta).filter(
            meta.shukkaRef.equal(s.getKey())).asList();
        return list;
    }
    
    public void deleteByShukka(Shukka s) {
        Transaction tx;
        Iterator<Key> keys = Datastore.query(meta).filter(
            meta.shukkaRef.equal(s.getKey())).asKeyIterator();
        while(keys.hasNext()) {
            Key key = keys.next();
            scService.deleteByShukkaKingaku(key);
            tx = Datastore.beginTransaction();
            Datastore.delete(tx, key);
            tx.commit();
        }
    }
}
