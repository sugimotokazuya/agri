package agri.controller.hanbai.keitai;

import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.hanbai.ShukkaKeitaiService;

public class EditController extends BaseController {

    private ShukkaKeitaiMeta meta = ShukkaKeitaiMeta.get();
    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    
    @Override
    public boolean validateAuth() {
        ShukkaKeitai p = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(p.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        ShukkaKeitai shukkaKeitai = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(shukkaKeitai, request);
        return forward("edit.jsp");
    }
}
