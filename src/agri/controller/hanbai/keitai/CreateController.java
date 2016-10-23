package agri.controller.hanbai.keitai;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;

public class CreateController extends BaseController {

    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<Torihikisaki> tList = tService.getAll(loginYago);
        List<Hinmoku> hList = hService.getAll(loginYago);
        
        StringBuffer sb = new StringBuffer();
        if(tList.size() == 0) {
            sb.append("【取引先】を登録してから出荷形態を行ってください。");   
        }
        if(hList.size() == 0) {
            sb.append("【品目】を登録してから出荷形態を行ってください。");
        }
        if(sb.length() > 0) {
            requestScope("errorStr", sb.toString());
            return forward("./");
        }
        
        requestScope("tList", tList);
        requestScope("hList", hList);
        return forward("create.jsp");
    }
}
