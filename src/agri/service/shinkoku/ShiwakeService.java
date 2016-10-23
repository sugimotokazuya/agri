package agri.service.shinkoku;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.shinkoku.ShiwakeMeta;
import agri.model.Yago;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;


public class ShiwakeService {

    private ShiwakeMeta meta = ShiwakeMeta.get();
    
    // 初期化用
    public List<Shiwake> getAllAll() {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(null)).asList();
    }
    
    // 初期化用
    public List<Shiwake> getInit(Yago yago, int year) {
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        start.set(year,  0, 1);
        Calendar end = Util.getCalendar();
        Util.resetTime(end);
        end.set(year + 1,  0, 1);
        
        return getByDateRangeB(yago, start.getTime(), end.getTime());
    }
    
    public Shiwake get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }

    public List<Shiwake> getLast100(Yago yago) {
        return Datastore.query(meta)
                .filter(meta.yagoRef.equal(yago.getKey()))
                .sort(
            meta.hiduke.desc, meta.key.desc).limit(100).asList();
    }
    
    public List<Shiwake> getByDateRangeB(Yago yago, Date start, Date end) {
        return Datastore.query(meta).filter(meta.hiduke.greaterThanOrEqual(start),
            meta.yagoRef.equal(yago.getKey()),
            meta.hiduke.lessThan(end)).sort(meta.hiduke.asc, meta.key.asc).asList();
    }
    
    public List<Shiwake> getByKariKamokuDateRange(Kamoku kamoku, Date start, Date end) {
        return Datastore.query(meta).filter(meta.hiduke.greaterThanOrEqual(start),
            meta.karikataKamokuRef.equal(kamoku.getKey()),
            meta.hiduke.lessThan(end)).sort(meta.hiduke.asc, meta.key.asc).asList();
    }
    
    public List<Shiwake> getByKashiKamokuDateRange(Kamoku kamoku, Date start, Date end) {
        return Datastore.query(meta).filter(meta.hiduke.greaterThanOrEqual(start),
            meta.kashikataKamokuRef.equal(kamoku.getKey()),
            meta.hiduke.lessThan(end)).sort(meta.hiduke.asc, meta.key.asc).asList();
    }
    
    public List<Shiwake> getByNendo(Yago yago, int nendo) {
        return Datastore.query(meta).filter(meta.nendo.equal(nendo),
            meta.yagoRef.equal(yago.getKey())).sort(meta.hiduke.asc, meta.key.asc).asList();
    }
    
//    public List<Shiwake> getByKarikata(Date start, Date end, Kamoku karikata) {
//        return Datastore.query(meta).filter(meta.hiduke.greaterThanOrEqual(start),
//            meta.hiduke.lessThan(end), 
//            meta.karikataKamokuRef.equal(karikata.getKey())).asList();
//    }
//    
//    public List<Shiwake> getByKashikata(Date start, Date end, Kamoku kashikata) {
//        return Datastore.query(meta).filter(meta.hiduke.greaterThanOrEqual(start),
//            meta.hiduke.lessThan(end), 
//            meta.kashikataKamokuRef.equal(kashikata.getKey())).asList();
//    }
    
    public List<Shiwake> getNoTotalUp(Yago yago, Date start, Date end) {
        
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.hiduke.greaterThanOrEqual(start),
            meta.hiduke.lessThan(end), meta.totalUp.equal(false)).sort(meta.hiduke.asc, meta.key.asc).asList();
    }
    
public List<Shiwake> getNoTotalUpByNendo(Yago yago, int nendo) {
        
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.nendo.equal(nendo),
            meta.totalUp.equal(false)).sort(meta.hiduke.asc, meta.key.asc).asList();
    } 
 
    public void insert(Shiwake shiwake) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shiwake);
        tx.commit();
    }

    public void update(Shiwake shiwake) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shiwake);
        tx.commit();
    }
    
    public void delete(Shiwake shiwake) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, shiwake.getKey());
        tx.commit();
    }
    
    public int getNendo(Yago yago, Calendar cal) {
        
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        start.set(yago.getShonendo(), yago.getStartMonth() - 1, 1);
        if(cal.before(start)) return cal.get(Calendar.YEAR);
        int nendo = cal.get(Calendar.YEAR);
        if((cal.get(Calendar.MONTH) + 1) < yago.getStartMonth()) {
            nendo --;
        }
        
        return nendo;
    }
}
