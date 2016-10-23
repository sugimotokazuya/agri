package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.service.Util;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SagyoService;
import agri.service.sagyo.SakudukeService;

public class IndexController extends BaseController {

    private SakudukeService sakudukeService = new SakudukeService();
    private SagyoService sagyoService = new SagyoService();
    private SagyoItemService sagyoItemService = new SagyoItemService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<Sakuduke> sakudukeList = sakudukeService.getInProgressList(loginYago);
        List<SagyoItem> sagyoItemList = sagyoItemService.getAll(loginYago);
        
        // 今日の作業
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        Calendar end = Util.getCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DAY_OF_MONTH, 1);
        List<Sagyo> todayList = sagyoService.getByDateRange(loginYago, start.getTime(), end.getTime());
        requestScope("todayList", todayList);
        
        // 昨日の作業
        end = Util.getCalendar();
        Util.resetTime(end);
        start = Util.getCalendar();
        start.setTime(end.getTime());
        start.add(Calendar.DAY_OF_MONTH, -1);
        List<Sagyo> yesterdayList = sagyoService.getByDateRange(loginYago, start.getTime(), end.getTime());
        requestScope("yesterdayList", yesterdayList);
        
        requestScope("sakudukeList", sakudukeList);
        requestScope("sagyoItemList", sagyoItemList);
        requestScope("loginUser", loginUser);
        
        return forward("index.jsp");
    }
}
