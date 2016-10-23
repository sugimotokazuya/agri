package agri.service.hanbai;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import agri.meta.hanbai.SeikyuMeta;
import agri.meta.hanbai.SeikyuRecMeta;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.Seikyu;
import agri.model.hanbai.SeikyuRec;
import agri.model.hanbai.Shukka;
import agri.service.Util;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class SeikyuService {

    private SeikyuMeta meta = SeikyuMeta.get();
    private SeikyuRecMeta recMeta = SeikyuRecMeta.get();
    private ShukkaService shukkaService = new ShukkaService();
    
    // TODO 初期化用
    public List<Seikyu> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public List<Seikyu> getByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta).filter(meta.torihikisakiRef.equal(t.getKey())).asList();
    }
    
    public Seikyu get(Key key, long version) {
        Seikyu s = Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
        return s;
    }
    
    public List<SeikyuRec> getRecList(Key key) {
        List<SeikyuRec> list = Datastore.query(recMeta).filter(
            recMeta.seikyuRef.equal(key)).asList();
        return list;
    }
    
    public void update(Seikyu s) {
        s.setTorihikisakiName(s.getTorihikisakiRef().getModel().getName());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
    }
    
    public void clearTorihikisaki(Seikyu s) {
        s.getTorihikisakiRef().clear();
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
    }
    
    public void delete(Key key, long version) {
        Seikyu s = Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
        deleteRec(s.getKey());
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, s.getKey());
        tx.commit();
    }
    
    private void deleteRec(Key parentKey) {
        List<SeikyuRec> delList = Datastore.query(recMeta).filter(
            recMeta.seikyuRef.equal(parentKey)).asList();
        Iterator<SeikyuRec> ite = delList.iterator();
        while(ite.hasNext()) {
            SeikyuRec rec = ite.next();
            rec.getShukkaRef().getModel().setKaishu(1);
            shukkaService.update(rec.getShukkaRef().getModel());
            Transaction tx = Datastore.beginTransaction();
            Datastore.delete(tx, rec.getKey());
            tx.commit();
        }
    }
    
    public List<Seikyu> getList(Yago yago, Integer year, Key tKey) {
        
        ModelQuery<Seikyu> mq = Datastore.query(meta);
        mq = mq.filter(meta.yagoRef.equal(yago.getKey()));
        if(year != null) {
            Calendar start = Util.getCalendar();
            start.clear();
            start.set(Calendar.YEAR, year);
            start.set(Calendar.MONTH, 0);
            start.set(Calendar.DAY_OF_MONTH, 1);
            Calendar end = Util.getCalendar();
            end.setTimeInMillis(start.getTimeInMillis());
            end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
            mq = mq.filter(meta.date.greaterThanOrEqual(start.getTime()),
            meta.date.lessThan(end.getTime()));
        }
        
        if(tKey != null) {
            mq = mq.filter(meta.torihikisakiRef.equal(tKey));
        }
        
        List<Seikyu> list =  mq.sort(meta.date.desc, meta.kaishu.asc).asList();
        
        return list;
    }
    
    public void insert(Yago yago, Date date, Date limitDate, Torihikisaki t, Key[] shukkaKeys) {
        Seikyu s = new Seikyu();
        s.getYagoRef().setModel(yago);
        s.setDate(date);
        s.setLimitDate(limitDate);
        s.getTorihikisakiRef().setModel(t);
        s.setTorihikisakiName(t.getName());
        
        List<ShukkaDto> dtoList = shukkaService.getDtoList(shukkaKeys);
        int seikyuKingaku = shukkaService.getSumGokei(dtoList);
        s.setSeikyuKingaku(seikyuKingaku);
     
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
        
        for(int i = 0; i < shukkaKeys.length; ++i) {
            tx = Datastore.beginTransaction();
            Shukka shukka = shukkaService.get(shukkaKeys[i]);
            SeikyuRec rec = new SeikyuRec();
            rec.getShukkaRef().setModel(shukka);
            rec.getSeikyuRef().setModel(s);
            Datastore.put(tx, rec);
            tx.commit();
            tx = Datastore.beginTransaction();
            shukka.setKaishu(2);
            Datastore.put(tx, shukka);
            tx.commit();
        }
    }
    
}
