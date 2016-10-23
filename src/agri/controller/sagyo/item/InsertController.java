package agri.controller.sagyo.item;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;

public class InsertController extends BaseController {

    private SagyoItemMeta meta = SagyoItemMeta.get();
    private SagyoItemService service = new SagyoItemService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        SagyoItem sagyoItem = new SagyoItem();
        BeanUtil.copy(request, sagyoItem);
        sagyoItem.getYagoRef().setModel(loginYago);
        service.insert(sagyoItem);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.tanni, v.maxlength(10));
        return v.validate();
    }
}
