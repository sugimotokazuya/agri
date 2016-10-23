package agri.controller.sagyo;

import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoMeta;
import agri.model.sagyo.Gazo;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.ShiyoKikai;
import agri.model.sagyo.ShiyoShizai;
import agri.service.UserService;
import agri.service.Util;
import agri.service.sagyo.GazoService;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SagyoService;

import com.google.appengine.api.datastore.KeyFactory;


public class EditController extends BaseController {

    private UserService userService = new UserService();
    private SagyoService sagyoService = new SagyoService();
    private GazoService gazoService = new GazoService();
    SagyoItemService sagyoItemService = new SagyoItemService();
    private SagyoMeta meta = SagyoMeta.get();
    
    @Override
    public boolean validateAuth() {
        Sagyo sagyo = sagyoService.get(asKey(meta.key), asLong(meta.version));
        if(!sagyo.getSakudukeRef().getModel().getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoEdit();
    }    
    @Override
    public Navigation run() throws Exception {
        
        Sagyo sagyo = sagyoService.get(asKey(meta.key), asLong(meta.version));
        requestScope("sagyo", sagyo);
        BeanUtil.copy(sagyo, request);
        Calendar cal = Util.getCalendar();
        cal.setTime(sagyo.getDate());
        requestScope("year", cal.get(Calendar.YEAR));
        requestScope("month", cal.get(Calendar.MONTH) + 1);
        requestScope("day", cal.get(Calendar.DAY_OF_MONTH));
        
        requestScope("userList", userService.getAll(loginYago));
        
        List<ShiyoShizai> ssList = sagyo.getShiyoShizaiListRef().getModelList();
        int i = 0;
        for(ShiyoShizai ss : ssList) {
            i++;
            requestScope("shizai" + i, KeyFactory.keyToString(ss.getShizaiRef().getKey()));
            requestScope("amount" + i, ss.getAmount());
        }
        
        List<ShiyoKikai> skList = sagyo.getShiyoKikaiListRef().getModelList();
        i = 0;
        for(ShiyoKikai sk : skList) {
            i++;
            requestScope("kikai" + i, KeyFactory.keyToString(sk.getKikaiRef().getKey()));
        }
        
        List<SagyoUser> suList = sagyo.getSagyoUserListRef().getModelList();
        i = 0;
        for(SagyoUser su : suList) {
            i++;
            requestScope("user" + i, KeyFactory.keyToString(su.getUserRef().getKey()));
            requestScope("minutes" + i, su.getMinutes());
        }
        
        List<Gazo> gazoList = gazoService.getByUser(loginUser); 
        List<Gazo> useGazoList = gazoService.getBySagyo(sagyo);
        requestScope("gazoList", gazoList);
        requestScope("useGazoList", useGazoList);
        SagyoItem sagyoItem = sagyoItemService.get(sagyo.getSagyoItemRef().getKey());
        requestScope("sagyoItem", sagyoItem);
        
        return forward("edit.jsp");
    }
}
