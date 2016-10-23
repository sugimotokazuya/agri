package agri.service.sehi;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import agri.meta.sehi.SokuteiMeta;
import agri.model.Yago;
import agri.model.sehi.Sekkei;
import agri.model.sehi.Sokutei;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SokuteiService {

    private SokuteiMeta meta = SokuteiMeta.get();
    private SekkeiService sekkeiService = new SekkeiService();
    
    public Sokutei get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    // TODO 初期化用
    public List<Sokutei> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public int getMinYear() {
        Calendar cal = Util.getCalendar();
        Date date = Datastore.query(meta).min(meta.date);
        if(date == null) {
            Calendar c = Util.getCalendar();
            date = c.getTime();
        }
        cal.setTimeInMillis(date.getTime());
        return cal.get(Calendar.YEAR);
    }
    
    public List<Sokutei> get(Yago yago, Date start, Date end, Key hatakeKey) {

        ModelQuery<Sokutei> mq = Datastore.query(meta);
        mq.filter(meta.yagoRef.equal(yago.getKey()));
        if(start != null) {
            mq.filter(meta.date.greaterThanOrEqual(start), meta.date.lessThan(end));
        }
        if(hatakeKey != null) {
            mq.filter(meta.hatakeRef.equal(hatakeKey));
        }
        return mq.sort(meta.date.desc).asList();
    }
    
    public void insert(Sokutei sokutei) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sokutei);
        tx.commit();
    }

    public Sokutei update(Sokutei sokutei) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sokutei);
        tx.commit();
        return sokutei;
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Sokutei sokutei = Datastore.get(tx, meta, key, version);
        List<Sekkei> sekkeiList = sekkeiService.getBySokutei(sokutei);
        Iterator<Sekkei> ite = sekkeiList.iterator();
        while(ite.hasNext()) {
            Sekkei sekkei = ite.next();
            sekkeiService.delete(sekkei.getKey(), sekkei.getVersion());
        }
        Datastore.delete(tx, sokutei.getKey());
        tx.commit();
    }
}
