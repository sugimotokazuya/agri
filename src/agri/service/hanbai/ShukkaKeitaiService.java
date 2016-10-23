package agri.service.hanbai;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.ShukkaKeitai;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShukkaKeitaiService {
    private ShukkaKeitaiMeta meta = ShukkaKeitaiMeta.get();
    
    public ShukkaKeitai get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public ShukkaKeitai get(Key key) {
        return Datastore.get(meta, key);
    }
    
   public List<ShukkaKeitai> get(Torihikisaki t, Hinmoku h) {
        return Datastore.query(meta).filter(meta.torihikisakiRef.equal(t.getKey()),
           meta.hinmokuRef.equal(h.getKey())).asList();
   }
   
   // 移行用
   public ShukkaKeitai get(Torihikisaki t, Hinmoku h, String tanni) {
       return Datastore.query(meta).filter(meta.torihikisakiRef.equal(t.getKey()),
          meta.hinmokuRef.equal(h.getKey()),
          meta.hoso.equal(tanni)).asSingle();
  }

   public List<ShukkaKeitai> getAllAll() {
       return Datastore.query(meta).asList();
   }
   
   public List<ShukkaKeitai> getAll(Yago yago) {
       return Datastore.query(meta)
               .filter(meta.yagoRef.equal(yago.getKey())
                   , meta.delete.equal(0))
               .sort(meta.juryo.asc).asList();
   }
   
    public List<ShukkaKeitai> getByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta)
                .filter(meta.torihikisakiRef.equal(t.getKey())
                    , meta.delete.equal(0))
                .sort(meta.juryo.asc).asList();
    }
    
    public List<ShukkaKeitai> getAllByHinmoku(Hinmoku h) {
        return Datastore.query(meta)
                .filter(meta.hinmokuRef.equal(h.getKey()))
                .sort(meta.juryo.asc).asList();
    }
    
    public List<ShukkaKeitai> getAllByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta)
                .filter(meta.torihikisakiRef.equal(t.getKey()))
                .sort(meta.juryo.asc).asList();
    }

    public void insert(ShukkaKeitai shukkaKeitai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shukkaKeitai);
        tx.commit();
    }

    public void update(ShukkaKeitai shukkaKeitai) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shukkaKeitai);
        tx.commit();
    }
    
    public void delete(ShukkaKeitai sk) {
        // JASの出荷量の問題がある
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, sk.getKey());
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        ShukkaKeitai shukkaKeitai = Datastore.get(tx, meta, key, version);
        shukkaKeitai.setDelete(1);
        Datastore.put(tx, shukkaKeitai);
        tx.commit();
    }
}
