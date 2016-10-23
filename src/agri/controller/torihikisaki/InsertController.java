package agri.controller.torihikisaki;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.TorihikisakiMeta;
import agri.model.Torihikisaki;
import agri.service.TorihikisakiService;

public class InsertController extends BaseController {

    private TorihikisakiMeta meta = TorihikisakiMeta.get();
    private TorihikisakiService service = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthHatakeEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        Torihikisaki torihikisaki = new Torihikisaki();
        BeanUtil.copy(request, torihikisaki);
        torihikisaki.getYagoRef().setModel(loginYago);
        service.insert(torihikisaki);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(22));
        v.add(meta.biko, v.maxlength(50));
        v.add(meta.nohin, v.required(), v.maxlength(1),v.integerType(), v.doubleRange(0, 5));
        v.add(meta.ikkatsuPrint, v.required(), v.maxlength(1),v.integerType(), v.doubleRange(0, 1));
        v.add(meta.address1, v.maxlength(15));
        v.add(meta.address2, v.maxlength(15));
        v.add(meta.address3, v.maxlength(15));
        v.add(meta.yubin1, v.maxlength(3));
        v.add(meta.yubin2, v.maxlength(4));
        v.add(meta.tel, v.maxlength(15));
        v.add(meta.soryo,  v.maxlength(4), v.integerType());
        v.add(meta.haitatsubi, v.maxlength(1), v.doubleRange(1, 2), v.integerType());
        v.add(meta.kiboujikan, v.maxlength(1), v.integerType());
        v.add(meta.iraiName, v.maxlength(30));
        v.add(meta.iraiAddress1, v.maxlength(17));
        v.add(meta.iraiAddress2, v.maxlength(17));
        v.add(meta.iraiYubin1, v.maxlength(3));
        v.add(meta.iraiYubin2, v.maxlength(4));
        v.add(meta.iraiTel, v.maxlength(15));
        return v.validate();
    }
}
