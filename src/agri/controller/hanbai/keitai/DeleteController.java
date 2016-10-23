package agri.controller.hanbai.keitai;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.hanbai.ShukkaKeitaiService;

public class DeleteController extends BaseController {

    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    private ShukkaKeitaiMeta meta = ShukkaKeitaiMeta.get();

    @Override
    public boolean validateAuth() {
        ShukkaKeitai p = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(p.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
