package agri.controller.hanbai.kakuduke;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;

public class ShukeiController extends BaseController {
   
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    private KakudukeService kService = new KakudukeService();
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        
        cal.set(Calendar.YEAR, asInteger("year"));
        cal.set(Calendar.MONTH, asInteger("month"));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        // 前月の計算をする
        cal.add(Calendar.MONTH, -1);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        
        kService.shukei(loginYago, year, month);

        return redirect("index");
    }
}
