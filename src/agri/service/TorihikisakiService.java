package agri.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.TorihikisakiMeta;
import agri.model.Chokubai;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.Seikyu;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.hanbai.SeikyuService;
import agri.service.hanbai.ShukkaKeitaiService;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;


public class TorihikisakiService {

    private TorihikisakiMeta meta = TorihikisakiMeta.get();
    private ChokubaiService chokubaiService = new ChokubaiService();
    private ShukkaKeitaiService skService = new ShukkaKeitaiService();
    private ShukkaService shukkaService = new ShukkaService();
    private SeikyuService seikyuService = new SeikyuService();
    
    public Torihikisaki get(Key key, Long version) {
        return Datastore.get(meta, key, version);
    }
    
    public Torihikisaki get(Key key) {
        return Datastore.get(meta, key);
    }

    public List<Torihikisaki> getAll(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.delete.equal(false)).sort(meta.name.asc).asList();
    }
    
    public List<Torihikisaki> getAllWithDeleted(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey())).sort(meta.name.asc).asList();
    }
    
    // TODO 初期化用
    public List<Torihikisaki> getAllAll() {
        return Datastore.query(meta).sort(meta.name.asc).asList();
    }
    
    public List<Torihikisaki> getChokubai(Yago yago) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.status.equal(0), meta.delete.equal(false))
            .sort(meta.name.asc).asList();
    }
    
    public Torihikisaki getByName(Yago yago, String name) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.name.equal(name)).asSingle();
    }

    public void insert(Torihikisaki torihikisaki) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, torihikisaki);
        tx.commit();
    }

    public void update(Torihikisaki t) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, t);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Torihikisaki t = Datastore.get(tx, meta, key, version);
        //関連するEntityの処理
        {
            List<Chokubai> chokubaiList = chokubaiService.getAllListByTorihikisaki(t);
            for(Chokubai c : chokubaiList) {
                chokubaiService.delete(c);
            }
            List<ShukkaKeitai> skList = skService.getAllByTorihikisaki(t);
            for(ShukkaKeitai sk : skList) {
                skService.delete(sk);
            }
            List<Shukka> shukkaList = shukkaService.getByTorihikisaki(t);
            for(Shukka s : shukkaList) {
                shukkaService.clearTorihikisaki(s);
            }
            List<Seikyu> seikyuList = seikyuService.getByTorihikisaki(t);
            for(Seikyu s : seikyuList) {
                seikyuService.clearTorihikisaki(s);
            }
        }
        Datastore.delete(tx, t.getKey());
        tx.commit();
    }
    
    public Torihikisaki updateDelete(Key key, Long version) {
        Transaction tx = Datastore.beginTransaction();
        Torihikisaki torihikisaki = Datastore.get(tx, meta, key, version);
        torihikisaki.setDelete(true);
        Datastore.put(tx, torihikisaki);
        tx.commit();
        return torihikisaki;
    }
}
