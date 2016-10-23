package agri.service.hanbai;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.ShukkaRecMeta;
import agri.model.Hinmoku;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaRec;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ShukkaRecService {
    
    private ShukkaRecMeta meta = ShukkaRecMeta.get();

    public List<ShukkaRec> getList(Shukka s) {
        
        List<ShukkaRec> list =  getList(s.getKey());
        return list;
    }
    
    public List<ShukkaRec> getByHinmoku(Hinmoku hinmoku, Date start, Date end) {
        List<ShukkaRec> list = Datastore.query(meta).filter(
            meta.hinmokuRef.equal(hinmoku.getKey()),
            meta.date.greaterThanOrEqual(start),
            meta.date.lessThan(end)
            ).asList();
        return list;
    }
    
    public List<ShukkaRec> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public List<ShukkaRec> getInit() {
        return Datastore.query(meta).filter(meta.date.equal(null)).asList();
    }
   
    public List<ShukkaRec> getList(Key key) {
        List<ShukkaRec> list =  Datastore.query(meta).filter(
            meta.shukkaRef.equal(key)).asList();
        Collections.sort(list, new ShukkaRecComparator());
        return list;
    }
    
    public List<ShukkaRec> getListByShukkaKeitai(Key skKey) {
        List<ShukkaRec> list =  Datastore.query(meta).filter(
            meta.shukkaKeitaiRef.equal(skKey)).asList();
        return list;
    }
    
    public void update(ShukkaRec sr) {
        sr.setHinmokuName(sr.getHinmokuRef().getModel().getName());
        sr.setHosoName(sr.getShukkaKeitaiRef().getModel().getHoso());
        sr.setDate(sr.getShukkaRef().getModel().getDate());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sr);
        tx.commit();
    }
    
    public class ShukkaRecComparator implements Comparator<ShukkaRec> {
        @Override
        public int compare(ShukkaRec a, ShukkaRec b) {
            
            int orderA = a.getHinmokuRef().getModel().getOrder();
            int orderB = b.getHinmokuRef().getModel().getOrder();
            
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
