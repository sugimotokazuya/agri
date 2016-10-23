package agri.service.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import agri.exception.UsedAlreadyException;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.Yago;
import agri.model.sagyo.Sakuduke;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SakudukeService {

    private SakudukeMeta meta = SakudukeMeta.get();
    
    public Sakuduke get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public Sakuduke get(Key key) {
        return Datastore.query(meta).filter(meta.key.equal(key)).asSingle();
    }
    
    // TODO 初期化用
    public List<Sakuduke> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public List<Sakuduke> getList(int year, Yago yago) {
        
        ModelQuery<Sakuduke> mq = Datastore.query(meta);
        mq = mq.filter(meta.yagoRef.equal(yago.getKey()));
        Calendar start = Util.getCalendar();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, 0);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTimeInMillis(start.getTimeInMillis());
        end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
        mq = mq.filter(meta.startDate.greaterThanOrEqual(start.getTime()),
        meta.startDate.lessThan(end.getTime()));
        
        List<Sakuduke> list =  mq.sort(
            meta.startDate.desc, meta.status.asc, meta.name.asc).asList();
        
        return list;
    }
    
    public List<Sakuduke> getInProgressList(Yago yago) {
        return Datastore.query(meta).filter(
            meta.status.equal(0), meta.yagoRef.equal(yago.getKey())).sort(
            meta.startDate.asc, meta.name.asc).asList();
    }
    
    public List<Sakuduke> getEasyInputList(Yago yago) {
        return Datastore.query(meta).filter(
            meta.status.equal(0), meta.yagoRef.equal(yago.getKey())).filter(
                meta.easyInput.equal(1)).sort(
            meta.startDate.asc, meta.name.asc).asList();
    }

    public void insert(Sakuduke sakuduke) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sakuduke);
        tx.commit();
    }
    
    public void update(Sakuduke sakuduke) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, sakuduke);
        tx.commit();
    }
    
    public void delete(Key key, Long version) throws UsedAlreadyException {
        Transaction tx = Datastore.beginTransaction();
        Sakuduke sakuduke = get(key, version);
        if(sakuduke.getSagyoListRef().getModelList().size() > 0) {
            throw new UsedAlreadyException();
        } else {
            Datastore.delete(key);
        }
        
        tx.commit();
    }
}
