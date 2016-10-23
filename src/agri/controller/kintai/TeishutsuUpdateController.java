package agri.controller.kintai;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.User;
import agri.model.kintai.Dakoku;
import agri.model.kintai.Kinmuhyo;
import agri.service.Util;
import agri.service.kintai.DakokuService;
import agri.service.kintai.KinmuhyoService;

public class TeishutsuUpdateController extends BaseController {

    private DakokuService dService = new DakokuService();
    private KinmuhyoService kService = new KinmuhyoService(); 
    
    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return forward("/kintai/teishutsu");
        }
        
        Kinmuhyo k = kService.get(asKey("key"), asLong("version"));
        Calendar cal = Util.getCalendar();
        cal.setTime(k.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        
        Date[] calRange = Util.createOneMonthRange(year, month);
        List<Dakoku> list = dService.getList(
            k.getUserRef().getModel(), calRange[0], calRange[1]);
        
        Iterator<Dakoku> ite = list.iterator();
        while(ite.hasNext()) {
            Dakoku d = ite.next();
            cal = Util.getCalendar();
            cal.setTime(d.getDate());
            String yasumi = asString("yasumi" + cal.get(Calendar.DAY_OF_MONTH));
            d.setYasumi(yasumi != null);
            String ohiru = asString("ohiru" + cal.get(Calendar.DAY_OF_MONTH));
            d.setOhiru(ohiru != null);
            dService.update(d);
        }
        
        k.setShonin(1); //申請中
        k.setRiyu("");
        kService.update(k);
        
        return redirect("/kintai");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        for(int i = 1; i < 32; ++i) {
            v.add("yasumi" + i, v.maxlength(10));
        }
        v.add("key", v.maxlength(50));
        v.add("version", v.longType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        Kinmuhyo k = kService.get(asKey("key"), asLong("version"));
        User user = k.getUserRef().getModel();
        if(!user.equals(loginUser) && !loginUser.isAuthKintai()) return false;
        return loginUser.isUseDakoku() || loginUser.isAuthKintai();
    }
}
