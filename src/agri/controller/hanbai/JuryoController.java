package agri.controller.hanbai;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Hinmoku;
import agri.model.hanbai.ShukkaRec;
import agri.service.HinmokuService;
import agri.service.Util;
import agri.service.hanbai.ShukkaRecService;

public class JuryoController extends BaseController {

    private HinmokuService hService = new HinmokuService();
    private ShukkaRecService srService = new ShukkaRecService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    protected Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        int yearI = asInteger("year") == null ? year : asInteger("year");
        cal = Util.getCalendar();
        requestScope("year", yearI);

        Map<Hinmoku, Double> map = new HashMap<Hinmoku, Double>();
        
        Date[] range = Util.createOneYearNendoRange(yearI);
        
        List<Hinmoku> hList = hService.getAll(loginYago);
        for(Hinmoku hinmoku : hList) {
            double juryo = 0; 
            List<ShukkaRec> recList = srService.getByHinmoku(hinmoku, range[0], range[1]);
            for(ShukkaRec rec : recList) {
                juryo += rec.getSuryo() * rec.getShukkaKeitaiRef().getModel().getJuryo();
            }
            map.put(hinmoku, juryo / 1000);
        }
        
        requestScope("hList", hList);
        requestScope("map", map);
        
        return forward("juryo.jsp");
    }

}
