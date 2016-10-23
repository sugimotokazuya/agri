package agri.controller.sagyo.item;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.sagyo.SagyoItemMeta;
import agri.model.sagyo.SagyoItem;
import agri.service.sagyo.SagyoItemService;

public class UpdateController extends BaseController {

    private SagyoItemMeta meta = SagyoItemMeta.get();
    private SagyoItemService service = new SagyoItemService();

    @Override
    public boolean validateAuth() {
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        if(!sagyoItem.getYagoRef().getModel().equals(loginYago)) return false;
        return loginUser.isAuthSagyoItemEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("edit.jsp");
        }
        SagyoItem sagyoItem = service.get(asKey(meta.key), asLong(meta.version));
        BeanUtil.copy(request, sagyoItem);
        service.update(sagyoItem);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add(meta.tanni, v.maxlength(10));
        return v.validate();
    }
}
