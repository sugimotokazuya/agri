package agri.service.hanbai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.hanbai.KakudukeSumMeta;
import agri.model.Yago;
import agri.model.hanbai.KakudukeSum;
import agri.service.Util;

import com.google.appengine.api.datastore.Transaction;

public class KakudukeSumService {
    
    //private KakudukeService kService = new KakudukeService();
    private KakudukeSumMeta meta = KakudukeSumMeta.get();

    public KakudukeSum get(int year, int month, Yago yago) {
        
        KakudukeSum ks = Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()),
            meta.year.equal(year),meta.month.equal(month)).asSingle();
        return ks;
    }
    
public List<KakudukeSum> getGreaterThanOrEqual(int year, int month, Yago yago) {
        
        List<KakudukeSum> list = Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()),
            meta.year.greaterThanOrEqual(year)).sort(
                meta.year.asc, meta.month.asc).asList();
        
        List<KakudukeSum> resultList = new ArrayList<KakudukeSum>();
        for(KakudukeSum ks : list) {
            if(ks.getYear() == year && ks.getMonth() < month) continue;
            resultList.add(ks);
        }
        return resultList;
    }
    
    public void update(Yago yago, int year, int month, int sum) {
        
        KakudukeSum ks = get(year, month, yago);
        if(ks == null) {
            Calendar cal = Util.getCalendar();
            Util.resetTime(cal);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.add(Calendar.MONTH, -1);
            KakudukeSum zenKs = get(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), yago);
            if(zenKs != null) {
                sum += zenKs.getSum();
            } else {
                return;
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
        
        // この月以降の残も変更する必要がある
        ks.setSum(ks.getSum() + sum);
        
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, ks);
        
        // もし前に作った残と差があればこの月以降の残も変更する必要がある
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(Calendar.YEAR, ks.getYear());
        cal.set(Calendar.MONTH, ks.getMonth());
        cal.add(Calendar.MONTH, 1);
        List<KakudukeSum> sumList = getGreaterThanOrEqual(cal.get(Calendar.YEAR), 
            cal.get(Calendar.MONTH), ks.getYagoRef().getModel());
        
        for(KakudukeSum kakudukeSum : sumList) {
            kakudukeSum.setSum(kakudukeSum.getSum() + sum);
            Datastore.put(tx, kakudukeSum);
        }

        tx.commit();
    }
    
    public void delete(KakudukeSum ks) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, ks.getKey());
        tx.commit();
    }
}
