package agri.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.HinmokuMeta;
import agri.meta.hanbai.ShukkaCountMeta;
import agri.meta.hanbai.UriageCountMeta;
import agri.model.Hinmoku;
import agri.model.Yago;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.hanbai.ShukkaKeitaiService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class HinmokuService {
    
    private HinmokuMeta meta = HinmokuMeta.get();
    private ShukkaKeitaiService skService = new ShukkaKeitaiService();
    
    public Hinmoku get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Hinmoku get(Key key) {
        return Datastore.get(meta, key);
    }
    
    public Hinmoku getByStartsWith(Yago yago, String name) {
        Hinmoku hinmoku = Datastore.query(meta).filter(
            meta.delete.equal(false),
            meta.yagoRef.equal(yago.getKey()),
            meta.name.startsWith(name)).asSingle();
        return hinmoku;
    }
    
    public Hinmoku getByName(Yago yago, String name) {
        Hinmoku hinmoku = Datastore.query(meta).filter(
            meta.delete.equal(false),
            meta.yagoRef.equal(yago.getKey()),
            meta.name.equal(name)).asSingle();
        return hinmoku;
    }

    public List<Hinmoku> getAll(Yago yago) {
        return Datastore.query(meta)
                .filter(
                    meta.delete.equal(false),
                    meta.yagoRef.equal(yago.getKey()))
                .sort(meta.order.asc).asList();
    }
    
    public List<Hinmoku> getAllWithDeleted(Yago yago) {
        return Datastore.query(meta)
                .filter(
                    meta.yagoRef.equal(yago.getKey()))
                .sort(meta.order.asc).asList();
    }
    
    public List<Hinmoku> getAllAll() {
        return Datastore.query(meta).sort(meta.order.asc).asList();
    }

    public void insert(Hinmoku hinmoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hinmoku);
        tx.commit();
    }

    public void update(Hinmoku hinmoku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hinmoku);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Hinmoku hinmoku = Datastore.get(tx, meta, key, version);
        Key hKey = hinmoku.getKey();
        
        boolean isUpdate = false;
        
        ShukkaCountMeta scMeta = ShukkaCountMeta.get();
        if(Datastore.query(scMeta).filter(scMeta.hinmokuRef.equal(hKey)).count() > 0) {
            isUpdate = true;
        }
        
        UriageCountMeta ucMeta = UriageCountMeta.get();
        if(Datastore.query(ucMeta).filter(ucMeta.hinmokuRef.equal(hKey)).count() > 0) {
            isUpdate = true;
        }
        
        if(!isUpdate) {
            List<ShukkaKeitai> skList = skService.getAllByHinmoku(hinmoku);
            for(ShukkaKeitai sk : skList) {
                skService.delete(sk);
            }
        }
        
        if(isUpdate) {
            hinmoku.setDelete(true);
            Datastore.put(tx, hinmoku);
        } else {
            Datastore.delete(tx, hinmoku.getKey());
        }
        tx.commit();
    }
    
    public void sort(List<Hinmoku> list) {
        Collections.sort(list, new HinmokuComparator());
    }
    
    public class HinmokuComparator implements Comparator<Hinmoku> {
        @Override
        public int compare(Hinmoku a, Hinmoku b) {
            
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
