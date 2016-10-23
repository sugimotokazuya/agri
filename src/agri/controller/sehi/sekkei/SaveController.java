package agri.controller.sehi.sekkei;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.sehi.SokuteiMeta;
import agri.model.sehi.Hiryo;
import agri.model.sehi.Sekkei;
import agri.model.sehi.Sokutei;
import agri.service.sehi.HiryoService;
import agri.service.sehi.SekkeiService;
import agri.service.sehi.SokuteiService;

import com.google.appengine.api.datastore.KeyFactory;

public class SaveController extends BaseController {

    private SokuteiMeta meta = SokuteiMeta.get();
    private SokuteiService sokuteiService = new SokuteiService();
    private HiryoService hiryoService = new HiryoService();
    private SekkeiService service = new SekkeiService();
    
    @Override
    public boolean validateAuth() {
        Sokutei sokutei = sokuteiService.get(asKey(meta.key), asLong(meta.version));
        if(!sokutei.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            Sokutei sokutei = sokuteiService.get(asKey(meta.key), asLong(meta.version));
            requestScope("sokutei", sokutei);
            List<Hiryo> hiryoList = hiryoService.getAll(loginYago);
            requestScope("hiryoList", service.getActiveHiryoList(sokutei, hiryoList));
            return forward("index.jsp");
        }
        Sokutei sokutei = sokuteiService.get(asKey(meta.key), asLong(meta.version));
        
        List<String[]> keyList = getKeys();
        List<Sekkei> sekkeiList = new ArrayList<Sekkei>();
        Iterator<String[]> ite = keyList.iterator();
        while(ite.hasNext()) {
            String[] keys = ite.next();
            String sehiryoStr = (String)request.getAttribute("sehiryo_" + keys[0] + "_" + keys[1]);
            if("".equals(sehiryoStr)) sehiryoStr="0";
            int sehiryo = Integer.parseInt(sehiryoStr);
            if(sehiryo == 0) continue;
            Sekkei sekkei = new Sekkei();
            sekkei.getSokuteiRef().setModel(sokutei);
            Hiryo hiryo = hiryoService.get(KeyFactory.stringToKey(keys[0]), Long.parseLong(keys[1]));
            sekkei.getHiryoRef().setModel(hiryo);
            int nkeisu = Integer.parseInt((String)request.getAttribute("nkeisu_" + keys[0] + "_" + keys[1]));
            sekkei.setNkeisu(nkeisu);
            sekkei.setSehiryo(sehiryo);
            sekkeiList.add(sekkei);
        }
        service.insert(sekkeiList, sokutei);
        return redirect("/sehi/sokutei");
    }
    
    private List<String[]> getKeys() {
        List<String[]> list = new ArrayList<String[]>();
        @SuppressWarnings("unchecked")
        Enumeration<String> enu = request.getAttributeNames();
        while(enu.hasMoreElements()) {
            String key = enu.nextElement();
            if(!key.startsWith("sehiryo_")) continue;
            String[] keys = new String[2];
            String[] strs = key.split("_");
            keys[0] = strs[1];
            keys[1] = strs[2];
            list.add(keys);
        }
        return list;
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        List<String[]> keysList = getKeys();
        Iterator<String[]> ite = keysList.iterator();
        while(ite.hasNext()) {
            String[] keys = ite.next();
            String nkeisuKey = "nkeisu_" + keys[0] + "_" + keys[1];
            String sehiryoKey = "sehiryo_" + keys[0] + "_" + keys[1];
            v.add(nkeisuKey, v.maxlength(3), v.integerType(), v.required(), v.doubleRange(0, 100));
            v.add(sehiryoKey, v.maxlength(4), v.integerType(), v.doubleRange(0, 9999));
        }
        return v.validate();
    }
}
