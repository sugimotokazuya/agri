package agri.service.hanbai;

import java.util.Calendar;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.KakudukeMeta;
import agri.model.Yago;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.KakudukeSum;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaRec;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class KakudukeService {
    
    private KakudukeMeta meta = KakudukeMeta.get();
    private KakudukeSumService ksService = new KakudukeSumService();
    
    public List<Kakuduke> getList(int year, int month, Yago yago) {
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.MONTH, 1);
        List<Kakuduke> list = Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()),
            meta.date.greaterThanOrEqual(start.getTime()),
            meta.date.lessThan(end.getTime()))
                .sort(meta.date.asc, meta.plus.desc)
                .asList();
        return list;
    }
    
    public int getMinYear(Yago yago) {
        Kakuduke k = Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()))
                .sort(meta.date.asc).limit(1).asSingle();
        Calendar cal = Util.getCalendar();
        if(k != null) {
            cal.setTime(k.getDate());
        }
        return cal.get(Calendar.YEAR);
    }
    
    public void shukei(Yago yago, int year, int month) {
        
        List<Kakuduke> list = getList(year, month, yago);
        
        int sum = 0;
        for(Kakuduke k : list) {
            sum += k.getPlus();
        }
        
        // 前月の残を足す
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.add(Calendar.MONTH, -1);
        KakudukeSum zenKs = ksService.get(cal.get(Calendar.YEAR), 
            cal.get(Calendar.MONTH), yago);
        if(zenKs != null) sum += zenKs.getSum();
    
        KakudukeSum ks = ksService.get(year, month, yago);
        if(ks == null) {
            cal.add(Calendar.MONTH, -1);
            zenKs = ksService.get(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), yago);
            if(zenKs != null) {
                sum += zenKs.getSum();
            }
            ks = new KakudukeSum();
            ks.getYagoRef().setModel(yago);
            ks.setYear(year);
            ks.setMonth(month);
            ks.setSum(sum);
            Transaction tx = Datastore.beginTransaction();
            Datastore.put(tx, ks);
            tx.commit();
            return;
        }
        
        int sabun = sum - ks.getSum();
        ks.setSum(sum);
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, ks);
        
        // もし前に作った残と差があればこの月以降の残も変更する必要がある
        if(sabun != 0) {
            cal = Util.getCalendar();
            Util.resetTime(cal);
            cal.set(Calendar.YEAR, ks.getYear());
            cal.set(Calendar.MONTH, ks.getMonth());
            cal.add(Calendar.MONTH, 1);
            List<KakudukeSum> sumList = ksService.getGreaterThanOrEqual(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), ks.getYagoRef().getModel());
            
            for(KakudukeSum kakudukeSum : sumList) {
                kakudukeSum.setSum(kakudukeSum.getSum() + sabun);
                Datastore.put(tx, kakudukeSum);
            }
        }

        tx.commit();
    }

    public Kakuduke getByShukka(Shukka s) {
        return Datastore.query(meta).filter(
            meta.shukkaRef.equal(s.getKey())).asSingle();
    }
    
    public Kakuduke getByShukka(Key shukkaKey) {
        
        return Datastore.query(meta).filter(
            meta.shukkaRef.equal(shukkaKey)).asSingle();
    }
    
    public Kakuduke getByShukkaRec(ShukkaRec rec) {
        return Datastore.query(meta).filter(
            meta.shukkaRecRef.equal(rec.getKey())).asSingle();
    }
    
    public Kakuduke get(Key key, long version) {
        return Datastore.get(meta, key, version);
    }
   
    public void insert(Kakuduke kakuduke) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, kakuduke);
        tx.commit();
        
        // 格付けの集計も更新する
        Calendar cal = Util.getCalendar();
        cal.setTime(kakuduke.getDate());
        ksService.update(kakuduke.getYagoRef().getModel(), 
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), kakuduke.getPlus());
        
    }
    
    public void delete(Kakuduke k) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, k.getKey());
        tx.commit();
        
        Calendar cal = Util.getCalendar();
        cal.setTime(k.getDate());
        int sum = k.getPlus() * -1;
        ksService.update(k.getYagoRef().getModel(), 
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), sum);
    }
}
