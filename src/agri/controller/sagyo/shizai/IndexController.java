package agri.controller.sagyo.shizai;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;

public class IndexController extends BaseController {

    private SagyoItemService service = new SagyoItemService();
    private SagyoItemMeta meta = SagyoItemMeta.get();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSagyoItemView();
    }
    
    @Override
    public Navigation run() throws Exception {
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        requestScope("sagyoItem", sagyoItem);
        requestScope("shizaiList", sagyoItem.getShizaiListRef().getModelList());
        return forward("index.jsp");
    }
}
