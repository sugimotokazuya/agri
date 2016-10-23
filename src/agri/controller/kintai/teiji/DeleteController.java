package agri.controller.kintai.teiji;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.kintai.TeijiMeta;
import agri.model.kintai.Teiji;
import agri.service.kintai.TeijiService;

public class DeleteController extends BaseController {

    private TeijiService service = new TeijiService();
    private TeijiMeta meta = TeijiMeta.get();
    
    @Override
    public boolean validateAuth() {
        Teiji teiji = service.get(asKey(meta.key), asLong(meta.version));
        if(!teiji.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthKintai();
    }
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
