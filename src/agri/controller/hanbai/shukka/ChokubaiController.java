package agri.controller.hanbai.shukka;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Shukka;
import agri.service.hanbai.ShukkaService;

public class ChokubaiController extends BaseController {

    private ShukkaService sService = new ShukkaService();
    /*private ChokubaiService cService = new ChokubaiService();
    private ShukkaRecService recService = new ShukkaRecService();
    private OneNoticeService onService = new OneNoticeService();
    private SokuhoService sokuhoService = new SokuhoService();*/
    
    @Override
    public boolean validateAuth() {
        Shukka shukka = sService.get(asKey("key"));
        if(!loginYago.equals(shukka.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        // 使っていない
        /*
        Key shukkaKey = asKey("key");
        Shukka shukka = sService.get(shukkaKey);
        
        List<Chokubai> chokubaiList = 
            cService.getListByTorihikisaki(shukka.getTorihikisakiRef().getModel());
        requestScope("chokubaiList", chokubaiList);
        requestScope("shopSize", chokubaiList.size());
        
        List<ShukkaRec> recList = recService.getList(shukka);
        requestScope("recList", recList);
        requestScope("recListSize", recList.size());
        requestScope("shukka", shukka);

        Calendar cal1 = Util.getCalendar();
        cal1.setTime(shukka.getDate());
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.add(Calendar.DAY_OF_MONTH, -1);
        Calendar cal2 = Util.getCalendar();
        cal2.setTime(shukka.getDate());
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        List<OneNotice> onList = 
                onService.getListByDateRange(loginYago, cal1.getTime(), cal2.getTime());
        requestScope("linkList", onList);
        
        SokuhoMapDto dto1 = sokuhoService.getSokuhoMap(loginYago, shukka.getDate(), 1);
        requestScope("dto1", dto1);
        
        Calendar cal = Util.getCalendar();
        Util.ResetTime(cal);
        cal.setTime(shukka.getDate());
        cal.set(Calendar.HOUR_OF_DAY, 7);
        
        SokuhoMapDto dto2 = sokuhoService.getSokuhoMap(loginYago, cal.getTime(), 0);
        requestScope("dto2", dto2);*/
        
        
        return forward("chokubai.jsp");
    }
}
