package agri.controller.hanbai.shukka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.Chokubai;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaCount;
import agri.model.hanbai.ShukkaKingaku;
import agri.model.hanbai.ShukkaRec;
import agri.service.ChokubaiService;
import agri.service.hanbai.ShukkaCountService;
import agri.service.hanbai.ShukkaKingakuService;
import agri.service.hanbai.ShukkaRecService;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ChokubaiInsertController extends BaseController {

    private ShukkaService service = new ShukkaService();
    private ChokubaiService cService = new ChokubaiService();
    private ShukkaRecService recService = new ShukkaRecService();
    private ShukkaKingakuService skService = new ShukkaKingakuService();
    private ShukkaCountService scService = new ShukkaCountService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = service.get(asKey("key"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Key shukkaKey = asKey("key");
        Shukka shukka = service.get(shukkaKey);
        
        if (!validate()) {
            String keyStr = KeyFactory.keyToString(shukka.getKey());
            requestScope("edit", true);
            return forward("chokubai/" + keyStr);
        }
        
        List<ShukkaRec> recList = recService.getList(shukka);
        List<Chokubai> chokubaiList = 
            cService.getListByTorihikisaki(shukka.getTorihikisakiRef().getModel());
        
        Iterator<ShukkaRec> recIte = recList.iterator();
        Map<Chokubai, ShukkaKingaku> skMap = new HashMap<Chokubai, ShukkaKingaku>();
        List<ShukkaCount> scList = new ArrayList<ShukkaCount>();
        while(recIte.hasNext()) {
            ShukkaRec rec = recIte.next();
            for(int i = 0; i < chokubaiList.size(); ++i) {
                Chokubai chokubai = chokubaiList.get(i);
                ShukkaKingaku sk;
                if(skMap.containsKey(chokubai)) {
                    sk = skMap.get(chokubai);
                } else {
                    sk = new ShukkaKingaku();
                    sk.getShukkaRef().setModel(shukka);
                    sk.getChokubaiRef().setModel(chokubai);
                    skMap.put(chokubai, sk);
                }
                String recKeyStr = KeyFactory.keyToString(rec.getKey()) 
                    + "_" + KeyFactory.keyToString(chokubai.getKey());
                ShukkaCount sc = new ShukkaCount();
                sc.setCount(asInteger(recKeyStr) == null ? 0 : asInteger(recKeyStr));
                sc.getHinmokuRef().setModel(rec.getHinmokuRef().getModel());
                sc.getShukkaKingakuRef().setModel(sk);
                scList.add(sc);
                sk.setKingaku(sk.getKingaku() + (rec.getTanka() * sc.getCount()));
            }
        }

        skService.deleteByShukka(shukka);
        
        Iterator<ShukkaKingaku> skIte = skMap.values().iterator();
        while(skIte.hasNext()) {
            ShukkaKingaku sk = skIte.next();
            skService.insert(sk);
        }
        
        Iterator<ShukkaCount> scIte = scList.iterator();
        while(scIte.hasNext()) {
            ShukkaCount sc = scIte.next();
            scService.insert(sc);
        }
        
        return redirect(basePath + "../");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        Key shukkaKey = asKey("key");
        Shukka shukka = service.get(shukkaKey);
        List<ShukkaRec> recList = recService.getList(shukka);
        List<Chokubai> chokubaiList = 
            cService.getListByTorihikisaki(shukka.getTorihikisakiRef().getModel());
        
        Iterator<ShukkaRec> recIte = recList.iterator();
        while(recIte.hasNext()) {
            ShukkaRec rec = recIte.next();
            int suryo = 0;
            for(int i = 0; i < chokubaiList.size(); ++i) {
                Chokubai chokubai = chokubaiList.get(i);
                String recKeyStr = KeyFactory.keyToString(rec.getKey()) 
                + "_" + KeyFactory.keyToString(chokubai.getKey());
                v.add(recKeyStr, v.integerType(), v.maxlength(4));
                suryo += asInteger(recKeyStr) == null ? 0 : asInteger(recKeyStr);
            }
            if(rec.getSuryo() != suryo) {
                requestScope("numError", true);
            }
        }
        
        return v.validate();
    }
}
