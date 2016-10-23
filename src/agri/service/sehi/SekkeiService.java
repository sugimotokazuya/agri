package agri.service.sehi;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;

import agri.meta.sehi.SekkeiMeta;
import agri.model.sehi.Hiryo;
import agri.model.sehi.Sekkei;
import agri.model.sehi.Sokutei;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class SekkeiService {

    private SekkeiMeta meta = SekkeiMeta.get();
    
    public Sekkei get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public List<Sekkei> getBySokutei(Sokutei sokutei) {
        return Datastore.query(meta).filter(
            meta.sokuteiRef.equal(sokutei.getKey())).asList();
    }
    
    public Sekkei get(Sokutei sokutei, Hiryo hiryo) {
        return Datastore.query(meta).filter(meta.sokuteiRef.equal(sokutei.getKey())
            , meta.hiryoRef.equal(hiryo.getKey())).asSingle();
    }
    
    public List<Hiryo> getActiveHiryoList(Sokutei sokutei, List<Hiryo> hiryoList) {
        return getActiveHiryoList(sokutei, hiryoList, null);
    }
    
    public List<Hiryo> getActiveHiryoList(Sokutei sokutei, List<Hiryo> hiryoList, HttpServletRequest request) {
        
        Iterator<Hiryo> ite = hiryoList.iterator();
        while(ite.hasNext()) {
            Hiryo hiryo = ite.next();
            Sekkei sekkei = this.get(sokutei, hiryo);
            if(sekkei == null && hiryo.isDelete()) {
                ite.remove();
                continue;
            }
            String key = KeyFactory.keyToString(hiryo.getKey())
            + "_" + String.valueOf(hiryo.getVersion());
            if(request == null) {
                continue;
            } else if(sekkei == null) {
                request.setAttribute("nkeisu_" + key, hiryo.getNkeisu());
            } else {
                request.setAttribute("nkeisu_" + key, sekkei.getNkeisu());
                request.setAttribute("sehiryo_" + key, sekkei.getSehiryo());
            }
        }
        return hiryoList;
    }
    
    private void deleteBySokutei(Sokutei sokutei) {
        
        List<Sekkei> beforeList = getBySokutei(sokutei);
        Iterator<Sekkei> ite = beforeList.iterator();
        while(ite.hasNext()) { 
            Transaction tx = Datastore.beginTransaction();
            Sekkei sekkei = ite.next();
            Datastore.delete(tx, sekkei.getKey());
            tx.commit();
        }
        
    }
    
    public void insert(List<Sekkei> list, Sokutei sokutei) {
        
        deleteBySokutei(sokutei);
        Iterator<Sekkei> ite = list.iterator();
        while(ite.hasNext()) {
            Transaction tx = Datastore.beginTransaction();
            Sekkei sekkei = ite.next();
            Datastore.put(tx, sekkei);
            tx.commit();
        }
        
    }

    public Sekkei update(Key key, Long version, Map<String, Object> input) {
        Transaction tx = Datastore.beginTransaction();
        Sekkei sekkei = Datastore.get(tx, meta, key, version);
        BeanUtil.copy(input, sekkei);
        Datastore.put(tx, sekkei);
        tx.commit();
        return sekkei;
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Sekkei sekkei = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, sekkei.getKey());
        tx.commit();
    }
}
