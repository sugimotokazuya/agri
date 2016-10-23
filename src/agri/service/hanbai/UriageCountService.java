package agri.service.hanbai;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.UriageCountMeta;
import agri.model.hanbai.UriageCount;
import agri.model.hanbai.UriageKingaku;

import com.google.appengine.api.datastore.Transaction;

public class UriageCountService {
    
    private UriageCountMeta meta = UriageCountMeta.get();
    
    public void insert(UriageCount uriageCount) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, uriageCount);
        tx.commit();
    }
    
    public List<UriageCount> getListByUriageKingaku(UriageKingaku uk) {
        List<UriageCount> list = Datastore.query(meta).filter(
            meta.uriageKingakuRef.equal(uk.getKey())).asList();
        return list;
    }
    
    public void delete(UriageCount uc) {
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, uc.getKey());
        tx.commit();
    }
}
