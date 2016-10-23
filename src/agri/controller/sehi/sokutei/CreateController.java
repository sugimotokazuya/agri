package agri.controller.sehi.sokutei;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.Hatake;
import agri.service.HatakeService;

public class CreateController extends BaseController {

    private HatakeService hatakeService = new HatakeService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        List<Hatake> hatakeList = hatakeService.getAll(loginYago);
        
        StringBuffer sb = new StringBuffer();
        if(hatakeList.size() == 0) {
            sb.append("【畑】を登録してから測定値登録を行ってください。");
        }
        if(sb.length() > 0) {
            requestScope("errorStr", sb.toString());
            return forward("./");
        }
        
        requestScope("hatakeList", hatakeList);
        return forward("create.jsp");
    }
}
