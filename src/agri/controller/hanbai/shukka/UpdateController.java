package agri.controller.hanbai.shukka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.User;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.Okurisaki;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaKeitai;
import agri.model.hanbai.ShukkaRec;
import agri.service.Const;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;
import agri.service.hanbai.OkurisakiService;
import agri.service.hanbai.ShukkaKeitaiService;
import agri.service.hanbai.ShukkaService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UpdateController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    private ShukkaService service = new ShukkaService();
    private ShukkaKeitaiService skService = new ShukkaKeitaiService();
    private KakudukeService kService = new KakudukeService();
    private OkurisakiService oService = new OkurisakiService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        Torihikisaki torihikisaki = tService.get(asKey("torihikisaki"));
        // ShukkaRecの品目の屋号チェックはとりあえず無し
        if(!loginYago.equals(torihikisaki.getYagoRef().getModel())) return false;
        //if(s.getKaishu() == 2) return false; 格付を変更したいのでOFF
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {

        Shukka shukka = service.get(asKey("key"), asLong("version"));
        int itemCount = asInteger("itemCount");
        Torihikisaki torihikisaki = tService.get(asKey("torihikisaki"));
        
        if (!validate()) {
            requestScope("tList", tService.getAll(loginYago));
            requestScope("hList", hService.getAll(loginYago));
            requestScope("taxConst", Const.TAX);
            
            Map<String, List<ShukkaKeitai>> skMap = new HashMap<String, List<ShukkaKeitai>>();
            List<ShukkaKeitai> skList = skService.getAll(loginYago);
            for(ShukkaKeitai sk : skList) {
                String key = KeyFactory.keyToString(sk.getTorihikisakiRef().getKey()) + ":"
                        + KeyFactory.keyToString(sk.getHinmokuRef().getKey());
                if(!skMap.containsKey(key)) {
                    skMap.put(key, new ArrayList<ShukkaKeitai>());
                }
                List<ShukkaKeitai> list = skMap.get(key);
                list.add(sk);
            }
            requestScope("skMap", skMap);
            
            @SuppressWarnings("rawtypes")
            List[] skListArray =  new ArrayList[itemCount];
            for(int i = 0; i < itemCount; ++i) {
                int j = i + 1;
                Key hinmokuKey = asKey("hinmoku" + j);
                Hinmoku hinmoku = hService.get(hinmokuKey);
                List<ShukkaKeitai> shukkaKeitaiList = skService.get(torihikisaki, hinmoku);
                skListArray[i] = shukkaKeitaiList;
            }
            requestScope("skListArray", skListArray);
            
            List<User> kakudukeTantoList = userService.getKakudukeTantoList(loginYago);
            requestScope("kakudukeTantoList", kakudukeTantoList);
            
            // 送り先を屋号で取得して取引先毎にMapに格納する
            Map<String, List<Okurisaki>> oMap = new HashMap<String, List<Okurisaki>>();
            List<Okurisaki> oList = oService.getByYago(loginYago);
            for(Okurisaki o : oList) {
                String key = KeyFactory.keyToString(o.getTorihikisakiRef().getKey());
                if(!oMap.containsKey(key)) {
                    oMap.put(key, new ArrayList<Okurisaki>());
                }
                List<Okurisaki> list = oMap.get(key);
                list.add(o);
            }
            requestScope("oMap", oMap);
            
            return forward("edit.jsp");
        }
        
        Calendar cal = Util.getCalendar();
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        Util.resetTime(cal);
        shukka.setDate(cal.getTime());
        shukka.getTorihikisakiRef().setModel(torihikisaki);
        shukka.setKaishu(torihikisaki.getStatus());
        shukka.setSoryo(asInteger("soryo"));
        shukka.setOkurisaki(asString("okurisaki"));
        shukka.setBiko(asString("biko"));
        shukka.setTax(asInteger("tax"));
        shukka.setKuchisu(asInteger("kuchisu"));
        
        List<Kakuduke> kList = new ArrayList<Kakuduke>();
        
        Key tantoKey = asKey("kakudukeTantoRef");
        Integer kakuduke = asInteger("kakuduke") == null ? 0 : asInteger("kakuduke");
        int kakudukeMaisu = 0;
        if(kakuduke > 0 && tantoKey != null) {
            Kakuduke k = new Kakuduke();
            k.setDate(shukka.getDate());
            k.setPlus(-kakuduke);
            k.getShukkaRef().setModel(shukka);
            k.getUserRef().setKey(tantoKey);
            k.getYagoRef().setModel(loginYago);
            kakudukeMaisu += kakuduke;
            kList.add(k);
        }
        
        List<ShukkaRec> list = new ArrayList<ShukkaRec>();
        for(int i = 1; i <= itemCount; ++i) {
            Integer tanka = asInteger("tanka" + i);
            Double suryo = asDouble("suryo" + i);
            if(tanka == null || suryo == null) continue;
            if(suryo == 0) continue;
            ShukkaRec rec = new ShukkaRec();
            rec.getShukkaRef().setModel(shukka);
            rec.getHinmokuRef().setKey(asKey("hinmoku" + i));
            rec.setTanka(tanka);
            rec.setSuryo(suryo);
            rec.getShukkaKeitaiRef().setKey(asKey("shukkaKeitai" + i));
            rec.setBiko(asString("biko" + i));
            list.add(rec);
            
            kakuduke = asInteger("kakuduke" + i) == null ? 0 : asInteger("kakuduke" + i);
            if(kakuduke > 0 && tantoKey != null) {
                Kakuduke k = new Kakuduke();
                k.setDate(shukka.getDate());
                k.setPlus(-kakuduke);
                k.getShukkaRecRef().setModel(rec);
                k.getUserRef().setKey(tantoKey);
                k.getYagoRef().setModel(loginYago);
                kakudukeMaisu += kakuduke;
                kList.add(k);
            }
        }
        
        shukka.setKakudukeMaisu(kakudukeMaisu);
        service.update(shukka, list);
        
        for(Kakuduke k : kList) {
            kService.insert(k);
        }
        
        return redirect(basePath + "../");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("year", v.required(), 
            v.longRange(2011, cal.get(Calendar.YEAR), "今年もしくは去年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        v.add("tanka1", v.required(), v.maxlength(5), v.integerType(), v.longRange(0, 99999));
        v.add("suryo1", v.required(), v.maxlength(5), v.doubleType(), v.doubleRange(0, 999));
        v.add("hinmoku1", v.maxlength(Const.KEY_LENGTH));
        v.add("shukkaKeitai1", v.maxlength(Const.KEY_LENGTH));
        v.add("kakuduke", v.maxlength(4), v.integerType(), v.doubleRange(1, 9999));
        v.add("kakuduke1", v.maxlength(4), v.integerType(), v.doubleRange(1, 9999));
        int itemCount = asInteger("itemCount");
        for(int i=2;i<=itemCount;i++) {
            v.add("tanka" + i, v.maxlength(5), v.integerType(), v.longRange(0, 99999));
            v.add("suryo" + i, v.maxlength(5), v.doubleType(), v.doubleRange(1, 999));
            v.add("hinmoku" + i, v.maxlength(Const.KEY_LENGTH));
            v.add("shukkaKeitai" + i, v.maxlength(Const.KEY_LENGTH));
            v.add("kakuduke" + i, v.maxlength(4), v.integerType(), v.doubleRange(1, 9999));
            v.add("biko" + i, v.maxlength(30));
        }
        v.add("soryo", v.required(), v.maxlength(4), v.integerType(), v.longRange(0, 9999));
        v.add("okurisaki", v.maxlength(30));
        v.add("biko", v.maxlength(100));
        v.add("kuchisu", v.required(), v.doubleRange(1, 9));
        return v.validate();
    }
}
