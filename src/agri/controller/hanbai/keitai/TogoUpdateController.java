package agri.controller.hanbai.keitai;

import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.hanbai.ShukkaKeitai;
import agri.model.hanbai.ShukkaRec;
import agri.service.Const;
import agri.service.hanbai.ShukkaKeitaiService;
import agri.service.hanbai.ShukkaRecService;

public class TogoUpdateController extends BaseController {

    private ShukkaKeitaiMeta meta = ShukkaKeitaiMeta.get();
    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    private ShukkaRecService srService = new ShukkaRecService();
    
    @Override
    public boolean validateAuth() {
        ShukkaKeitai sk = service.get(asKey(meta.key), asLong(meta.version));
        if(!loginYago.equals(sk.getYagoRef().getModel())) return false;
        
        ShukkaKeitai togoSk = service.get(asKey("togoKey"));
        if(!sk.getTorihikisakiRef().getKey().equals(togoSk.getTorihikisakiRef().getKey())) return false;
        if(!sk.getHinmokuRef().getKey().equals(togoSk.getHinmokuRef().getKey())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            ShukkaKeitai shukkaKeitai = service.get(asKey(meta.key), asLong(meta.version));
            BeanUtil.copy(shukkaKeitai, request);
            
            List<ShukkaKeitai> list = service.get(
                shukkaKeitai.getTorihikisakiRef().getModel(),
                shukkaKeitai.getHinmokuRef().getModel());
            requestScope("list", list);
            return forward("togo.jsp");
        }
        
        ShukkaKeitai sk = service.get(asKey(meta.key), asLong(meta.version));
        ShukkaKeitai togoSk = service.get(asKey("togoKey"));
        
        List<ShukkaRec> recList = srService.getListByShukkaKeitai(togoSk.getKey());
        for(ShukkaRec rec : recList) {
            rec.getShukkaKeitaiRef().setKey(sk.getKey());
            srService.update(rec);
        }
        service.delete(togoSk.getKey(), togoSk.getVersion());
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.key, v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add(meta.version, v.required(), v.maxlength(6), v.longType());
        v.add("togoKey", v.required(), v.maxlength(Const.KEY_LENGTH));
        return v.validate();
    }
}
