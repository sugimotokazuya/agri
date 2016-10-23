package agri.service.sagyo;

import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.sagyo.SagyoUserMeta;
import agri.model.User;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoUser;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SagyoUserService {
    
    private SagyoUserMeta meta = SagyoUserMeta.get();
    
    // 移行用
    public List<SagyoUser> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public void insert(SagyoUser sagyoUser) {
        sagyoUser.setDate(sagyoUser.getSagyoRef().getModel().getDate());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sagyoUser);
        tx.commit();
    }
    
    public void update(SagyoUser sagyoUser) {
        sagyoUser.setDate(sagyoUser.getSagyoRef().getModel().getDate());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sagyoUser);
        tx.commit();
    }
    
    public List<SagyoUser> getListBySagyo(Sagyo sagyo) {
        
        return Datastore.query(meta).filter(meta.sagyoRef.equal(
            sagyo.getKey())).asList();    
    }
    
    public List<SagyoUser> getListByUser(User user, int year) {
        
        Date[] range = Util.createOneYearRange(year);
        
        return Datastore.query(meta).filter(meta.userRef.equal(user.getKey())
            ,meta.date.greaterThanOrEqual(range[0])
            ,meta.date.lessThan(range[1]))
                .sort(meta.date.asc).asList();
    }
    
    public void delete(Key key) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(key);
        tx.commit();
    }
}
