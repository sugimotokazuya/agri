package agri.controller.kintai.teiji;

import java.util.Calendar;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.kintai.TeijiMeta;
import agri.model.kintai.Teiji;
import agri.service.Util;
import agri.service.kintai.TeijiService;


public class EditController extends BaseController {

    private TeijiMeta meta = TeijiMeta.get();
    private TeijiService service = new TeijiService();
    
    @Override
    public boolean validateAuth() {
        Teiji teiji = service.get(asKey(meta.key), asLong(meta.version));
        if(!teiji.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        Teiji teiji = service.get(asKey(meta.key), asLong(meta.version));
        
        Calendar start = Util.getCalendar();
        start.setTime(teiji.getStart());
        requestScope("startYear",start.get(Calendar.YEAR));
        requestScope("startMonth", start.get(Calendar.MONTH) + 1);
        requestScope("startDay", start.get(Calendar.DAY_OF_MONTH));
        
        Calendar end = Util.getCalendar();
        end.setTime(teiji.getEnd());
        requestScope("endYear",end.get(Calendar.YEAR));
        requestScope("endMonth", end.get(Calendar.MONTH) + 1);
        requestScope("endDay", end.get(Calendar.DAY_OF_MONTH));
        
        Calendar startTime = Util.getCalendar();
        startTime.setTime(teiji.getStartTime());
        requestScope("startTime1", startTime.get(Calendar.HOUR_OF_DAY));
        requestScope("startTime2", startTime.get(Calendar.MINUTE));
        
        Calendar endTime = Util.getCalendar();
        endTime.setTime(teiji.getEndTime());
        requestScope("endTime1", endTime.get(Calendar.HOUR_OF_DAY));
        requestScope("endTime2", endTime.get(Calendar.MINUTE));
        
        return forward("edit.jsp");
    }
}
