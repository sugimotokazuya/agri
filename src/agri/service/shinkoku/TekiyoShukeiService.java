package agri.service.shinkoku;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.shinkoku.TekiyoShukeiMeta;
import agri.model.Yago;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.model.shinkoku.Tekiyo;
import agri.model.shinkoku.TekiyoShukei;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class TekiyoShukeiService {

    private TekiyoShukeiMeta meta = TekiyoShukeiMeta.get();
    private ShiwakeService shiwakeService = new ShiwakeService();
    
    // 初期化用
    public List<TekiyoShukei> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public void shukei(Yago yago, int year) {
        
        List<Shiwake> noTotalUpList = 
                shiwakeService.getNoTotalUpByNendo(yago, year);
        
        for (Shiwake shiwake : noTotalUpList) {
            Tekiyo tekiyo = shiwake.getTekiyoRef().getModel();
            TekiyoShukei kariShukei = this.get(year,
                shiwake.getKarikataKamokuRef().getModel(), tekiyo);
            if(kariShukei == null) {
                kariShukei = new TekiyoShukei();
                kariShukei.getYagoRef().setModel(yago);
                kariShukei.setYear(year);
                kariShukei.getKamokuRef().setModel(
                    shiwake.getKarikataKamokuRef().getModel());
                kariShukei.getTekiyoRef().setModel(tekiyo);
            }
            kariShukei.setKarikata(kariShukei.getKarikata()
                + shiwake.getKarikataKingaku());
            
            TekiyoShukei kashiShukei = this.get(year,
                shiwake.getKashikataKamokuRef().getModel(), tekiyo);
            if(kashiShukei == null) {
                kashiShukei = new TekiyoShukei();
                kashiShukei.getYagoRef().setModel(yago);
                kashiShukei.setYear(year);
                kashiShukei.getKamokuRef().setModel(
                    shiwake.getKashikataKamokuRef().getModel());
                kashiShukei.getTekiyoRef().setModel(tekiyo);
            }
            kashiShukei.setKashikata(kashiShukei.getKashikata()
                + shiwake.getKashikataKingaku());
            
            shiwake.setTotalUp(true);
            shiwakeService.update(shiwake);
            this.update(kariShukei);
            this.update(kashiShukei);
        }
    }
//    
//    public void resetShukei(int year, Shiwake shiwake) {
//        Calendar start = Calendar.getInstance();
//        start.clear();
//        start.set(Calendar.YEAR, year);
//        start.set(Calendar.MONTH, 0);
//        start.set(Calendar.DAY_OF_MONTH, 1);
//        Calendar end = Calendar.getInstance();
//        end.setTimeInMillis(start.getTimeInMillis());
//        end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
//        
//        List<Shiwake> karikataSList =
//            shiwakeService.getByKarikata(start.getTime(), end.getTime(), 
//                shiwake.getKarikataKamokuRef().getModel());
//        for(Shiwake karikata : karikataSList) {
//            karikata.setTotalUp(false);
//            shiwakeService.update(karikata);
//        }
//        
//        List<Shiwake> kashikataSList =
//            shiwakeService.getByKashikata(start.getTime(), end.getTime(), 
//                shiwake.getKashikataKamokuRef().getModel());
//        for(Shiwake kashiikata : kashikataSList) {
//            kashiikata.setTotalUp(false);
//            shiwakeService.update(kashiikata);
//        }
//        
//        List<TekiyoShukei> karikataList = 
//            this.getList(year, shiwake.getKarikataKamokuRef().getModel());
//        for(TekiyoShukei shukei : karikataList) {
//            this.delete(shukei.getKey(), shukei.getVersion());
//        }
//        List<TekiyoShukei> kashikataList = 
//            this.getList(year, shiwake.getKashikataKamokuRef().getModel());
//        for(TekiyoShukei shukei : kashikataList) {
//            this.delete(shukei.getKey(), shukei.getVersion());
//        }
//    }
//    
    public void mainasuShukei(int year, Shiwake shiwake) {
        if(!shiwake.isTotalUp()) return;
        
        TekiyoShukei kariShukei = this.get(year,
            shiwake.getKarikataKamokuRef().getModel(), shiwake.getTekiyoRef().getModel());
        kariShukei.setKarikata(kariShukei.getKarikata() - shiwake.getKarikataKingaku());
        this.update(kariShukei);
        
        TekiyoShukei kashiShukei = this.get(year,
            shiwake.getKashikataKamokuRef().getModel(), shiwake.getTekiyoRef().getModel());
        kashiShukei.setKashikata(kashiShukei.getKashikata() - shiwake.getKashikataKingaku());
        this.update(kashiShukei);
    }
    
    public void resetShukei(Yago yago, int year) {
        
        List<Shiwake> shiwakeList = 
                shiwakeService.getByNendo(yago, year);

        for(Shiwake shiwake : shiwakeList) {
            shiwake.setTotalUp(false);
            shiwakeService.update(shiwake);
        }
        
        List<TekiyoShukei> shukeiList = this.getList(yago, year);
        for(TekiyoShukei shukei : shukeiList) {
            this.delete(shukei.getKey(), shukei.getVersion());
        }  
    }
    
    public TekiyoShukei get(int year,Kamoku kamoku, Tekiyo tekiyo) {
        return Datastore.query(meta).filter(meta.year.equal(year),
            meta.kamokuRef.equal(kamoku.getKey()), meta.tekiyoRef.equal(tekiyo.getKey())).asSingle();
    }
    
    public List<TekiyoShukei> getList(int year, Kamoku kamoku) {
        return Datastore.query(meta).filter(meta.year.equal(year),
            meta.kamokuRef.equal(kamoku.getKey())).asList();
    }
    
    public List<TekiyoShukei> getList(Yago yago, int year) {
        return Datastore.query(meta).filter(
            meta.year.equal(year), meta.yagoRef.equal(yago.getKey())).asList();
    }
    
    public int getMinYear(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey())).min(meta.year).intValue();
    }
    
    public void update(TekiyoShukei shukei) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, shukei);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        TekiyoShukei shukei = Datastore.get(tx, meta, key, version);
        Datastore.delete(tx, shukei.getKey());
        tx.commit();
    }

}
