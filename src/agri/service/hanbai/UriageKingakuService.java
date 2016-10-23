package agri.service.hanbai;

import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.UriageKingakuMeta;
import agri.model.hanbai.UriageCount;
import agri.model.hanbai.UriageKingaku;
import agri.model.mail.OneNotice;

import com.google.appengine.api.datastore.Transaction;

public class UriageKingakuService {

    private UriageKingakuMeta meta = UriageKingakuMeta.get();
    private UriageCountService ucService = new UriageCountService();
    
    public void insert(UriageKingaku uriageKingaku) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, uriageKingaku);
        tx.commit();
    }

    
    public List<UriageKingaku> getListByOneNotice(OneNotice on) {
        List<UriageKingaku> list = Datastore.query(meta).filter(
            meta.oneNoticeRef.equal(on.getKey())).asList();
        return list;
    }
    
    public void delete(UriageKingaku uk) {
        
        List<UriageCount> ucList = ucService.getListByUriageKingaku(uk);
        Iterator<UriageCount> ucIte = ucList.iterator();
        while(ucIte.hasNext()) {
            UriageCount uc = ucIte.next();
            ucService.delete(uc);
        }
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, uk.getKey());
        tx.commit();
    }
}
