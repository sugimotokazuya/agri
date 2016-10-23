package agri.controller.chokubai;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.ChokubaiMeta;
import agri.model.Chokubai;
import agri.model.Yago;
import agri.service.ChokubaiService;

public class EditController extends BaseController {

    private ChokubaiMeta meta = ChokubaiMeta.get();
    private ChokubaiService service = new ChokubaiService();
    
    @Override
    public boolean validateAuth() {
        Chokubai chokubai = service.get(asKey(meta.key), asLong(meta.version));
        Yago yago = chokubai.getTorihikisakiRef().getModel().getYagoRef().getModel();
        if(!loginYago.equals(yago)) return false;
        return loginUser.isAuthTorihikisakiEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        Chokubai chokubai = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(chokubai, request);
        return forward("edit.jsp");
    }
}
