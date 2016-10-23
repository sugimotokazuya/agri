package agri.controller.kintai.teiji;

import java.util.Calendar;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.kintai.TeijiMeta;
import agri.model.kintai.Teiji;
import agri.service.Util;
import agri.service.kintai.TeijiService;

public class UpdateController extends BaseController {

    private TeijiService teijiService = new TeijiService();
    private TeijiMeta meta = TeijiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Teiji teiji = teijiService.get(asKey(meta.key), asLong(meta.version));
        if(!teiji.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        
        Teiji teiji = teijiService.get(asKey(meta.key), asLong(meta.version));
        
        Calendar startDate = Util.getCalendar();
        Util.resetTime(startDate);
        startDate.set(Calendar.YEAR, asInteger("startYear"));
        startDate.set(Calendar.MONTH, asInteger("startMonth") - 1);
        startDate.set(Calendar.DAY_OF_MONTH, asInteger("startDay"));
        teiji.setStart(startDate.getTime());
        
        Calendar endDate = Util.getCalendar();
        Util.resetTime(endDate);
        endDate.set(Calendar.YEAR, asInteger("endYear"));
        endDate.set(Calendar.MONTH, asInteger("endMonth") - 1);
        endDate.set(Calendar.DAY_OF_MONTH, asInteger("endDay"));
        teiji.setEnd(endDate.getTime());
        
        Calendar startTime = Util.getCalendar();
        Util.resetTime(startTime);
        startTime.set(Calendar.HOUR_OF_DAY, asInteger("startTime1"));
        startTime.set(Calendar.MINUTE, asInteger("startTime2"));
        teiji.setStartTime(startTime.getTime());
        
        Calendar endTime = Util.getCalendar();
        Util.resetTime(endTime);
        endTime.set(Calendar.HOUR_OF_DAY, asInteger("endTime1"));
        endTime.set(Calendar.MINUTE, asInteger("endTime2"));
        teiji.setEndTime(endTime.getTime());
        
        teijiService.update(teiji);
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        Calendar cal = Util.getCalendar();
        v.add("startYear", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR) + 1, "前年分から翌年分までしか入力できません。"));
        v.add("startMonth", v.required(), v.longRange(1, 12));
        v.add("startDay", v.required(), v.longRange(1, 31));
        
        v.add("endYear", v.required(), 
            v.longRange(cal.get(Calendar.YEAR) - 1, cal.get(Calendar.YEAR) + 1, "前年分から翌年分までしか入力できません。"));
        v.add("endMonth", v.required(), v.longRange(1, 12));
        v.add("endDay", v.required(), v.longRange(1, 31));
        
        v.add("startTime1", v.required(), v.maxlength(2),v.longRange(0, 24));
        v.add("startTime2", v.required(), v.maxlength(2),v.longRange(0, 30));
        v.add("endTime1", v.required(), v.maxlength(2),v.longRange(0, 24));
        v.add("endTime2", v.required(), v.maxlength(2),v.longRange(0, 30));
        
        return v.validate();
    }
}
