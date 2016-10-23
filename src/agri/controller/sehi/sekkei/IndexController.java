package agri.controller.sehi.sekkei;

import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sehi.SokuteiMeta;
import agri.model.sehi.Hiryo;
import agri.model.sehi.Sokutei;
import agri.service.sehi.HiryoService;
import agri.service.sehi.SekkeiService;
import agri.service.sehi.SokuteiService;

public class IndexController extends BaseController {

    private SokuteiMeta meta = SokuteiMeta.get();
    
    private SokuteiService service = new SokuteiService();
    private SekkeiService sekkeiService = new SekkeiService();
    private HiryoService hiryoService = new HiryoService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSehiView();
    }
    
    @Override
    public Navigation run() throws Exception {
        Sokutei sokutei = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("sokutei", sokutei);
        List<Hiryo> hiryoList = hiryoService.getAll(loginYago);
        requestScope("hiryoList", sekkeiService.getActiveHiryoList(sokutei, hiryoList, request));
        return forward("index.jsp");
    }
}
