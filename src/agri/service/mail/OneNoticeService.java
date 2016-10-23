package agri.service.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.mail.OneNoticeMeta;
import agri.model.Yago;
import agri.model.hanbai.UriageKingaku;
import agri.model.mail.OneNotice;
import agri.service.Util;
import agri.service.hanbai.UriageKingakuService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class OneNoticeService {

    private OneNoticeMeta meta = OneNoticeMeta.get();
    private UriageKingakuService ukService = new UriageKingakuService();
    
    // TODO 初期化用
    public List<OneNotice> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public void insert(OneNotice oneNotice) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, oneNotice);
        tx.commit();
    }
    
    public void update(OneNotice on) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, on);
        tx.commit();
    }
    
    public void delete(OneNotice on) {
        
        List<UriageKingaku> ukList = ukService.getListByOneNotice(on);
        Iterator<UriageKingaku> ukIte = ukList.iterator();
        while(ukIte.hasNext()) {
            UriageKingaku uk = ukIte.next();
            ukService.delete(uk);
        }
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, on.getKey());
        tx.commit();
    }
    
    
    public int getSumUriageKingaku(Yago yago, int year, Key tKey) {
        
        int sum = 0;
        List<OneNotice> onList = getBy7hours(yago, year);
        for(OneNotice on : onList) {
            List<UriageKingaku> ukList = ukService.getListByOneNotice(on);
            for(UriageKingaku uk : ukList) {
                if(!uk.getChokubaiRef().getModel().getTorihikisakiRef().getKey().equals(tKey)) {
                    continue;
                }
                sum += uk.getKingaku();
            }
        }
        return sum;
    }
    
    public OneNotice getByDate(Yago yago, Date date) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.equal(date)).limit(1).asSingle();
    }
    
    public OneNotice get(Key key, long version) {
        return Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
    }
    
    public List<OneNotice> getByNearLessDate(Yago yago, Date date) {
        List<OneNotice> list = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.lessThanOrEqual(date)).sort(meta.date.desc).asList();
        return list;
    }
    
    public List<OneNotice> getBy7hours(Yago yago, Calendar startCal, Calendar endCal) {
        List<OneNotice> list = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.greaterThanOrEqual(startCal.getTime()),
            meta.date.lessThan(endCal.getTime())).sort(meta.date.asc).asList();
        List<OneNotice> removeList = new ArrayList<OneNotice>();
        for(OneNotice on : list) {
            Calendar tmpCal = Util.getCalendar();
            tmpCal.setTime(on.getDate());
            int hour = tmpCal.get(Calendar.HOUR_OF_DAY);
            if(hour != 7) {
                removeList.add(on);
            }
        }
        for(OneNotice remove : removeList) {
            list.remove(remove);
        }
        
        return list;
    }
    
    public List<OneNotice> getBy7hours(Yago yago, int year) {
        Calendar cal = Util.getCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Util.resetTime(cal);
        Calendar endCal = Util.getCalendar();
        endCal.setTime(cal.getTime());
        endCal.add(Calendar.YEAR, 1);
        
        List<OneNotice> list = getBy7hours(yago, cal, endCal);
        
        return list;
    }
    
    public OneNotice getByMinDate(Yago yago) {
        OneNotice on = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()))
            .sort(meta.date.asc).limit(1).asSingle();
        return on;
    }
    
    public List<OneNotice> getListByDateRange(Yago yago, Date start, Date end) {
        List<OneNotice> list = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.greaterThanOrEqual(start), meta.date.lessThan(end)).sort(meta.date.desc).asList();
        return list;
    }
    
    public List<OneNotice> getListByLast5(Yago yago) {
         List<OneNotice> list = Datastore.query(meta).filter(
             meta.yagoRef.equal(yago.getKey())).limit(5)
             .sort(meta.date.desc).asList();
         return list;
    }
}
