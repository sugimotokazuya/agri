package agri.controller.yago;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import agri.meta.YagoMeta;
import agri.service.YagoService;

public class DeleteController extends Controller {

    private YagoService service = new YagoService();
    private YagoMeta meta = YagoMeta.get();
    
    @Override
    public Navigation run() throws Exception {
        service.delete(asKey(meta.key), asLong(meta.version));
        return redirect(basePath);
    }
}
