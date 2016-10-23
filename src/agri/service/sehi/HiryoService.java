package agri.service.sehi;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sehi.HiryoMeta;
import agri.model.Yago;
import agri.model.sehi.Hiryo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class HiryoService {

    private HiryoMeta meta = HiryoMeta.get();
    
    public Hiryo get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public List<Hiryo> getAllAll() {
        return Datastore.query(meta).sort(
            meta.delete.asc
            ,meta.seibunN.desc
            ,meta.seibunP.desc
            ,meta.seibunK.desc
            ,meta.seibunCa.desc
            ,meta.seibunMg.desc
            ,meta.seibunB.desc
            ,meta.seibunMn.desc
            ,meta.seibunFe.desc
            ,meta.weight.desc).asList();
    }
    
    public List<Hiryo> getAll(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey())).sort(
            meta.delete.asc
            ,meta.seibunN.desc
            ,meta.seibunP.desc
            ,meta.seibunK.desc
            ,meta.seibunCa.desc
            ,meta.seibunMg.desc
            ,meta.seibunB.desc
            ,meta.seibunMn.desc
            ,meta.seibunFe.desc
            ,meta.weight.desc).asList();
    }

    public List<Hiryo> getNotDelete(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.delete.equal(false)).sort(
            meta.seibunN.desc
            ,meta.seibunP.desc
            ,meta.seibunK.desc
            ,meta.seibunCa.desc
            ,meta.seibunMg.desc
            ,meta.seibunB.desc
            ,meta.seibunMn.desc
            ,meta.seibunFe.desc
            ,meta.weight.desc).asList();
    }
    
    public void insert(Hiryo hiryo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hiryo);
        tx.commit();
    }

    public void update(Hiryo hiryo) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, hiryo);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        
        Transaction tx = Datastore.beginTransaction();
        Hiryo hiryo = Datastore.get(tx, meta, key, version);
        int count = hiryo.getSekkeiListRef().getModelList().size();
        
        if(count > 0) {
            hiryo.setDelete(true);
            Datastore.put(hiryo);
        } else {
            Datastore.delete(tx, hiryo.getKey());
        }
        tx.commit();
    }
}
