package agri.controller.hanbai.keitai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.hanbai.ShukkaKeitaiMeta;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.hanbai.ShukkaKeitai;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.hanbai.ShukkaKeitaiService;

public class InsertController extends BaseController {

    private ShukkaKeitaiMeta meta = new ShukkaKeitaiMeta();
    private ShukkaKeitaiService service = new ShukkaKeitaiService();
    private TorihikisakiService tService = new TorihikisakiService();
    private HinmokuService hService = new HinmokuService();
    
    @Override
    public boolean validateAuth() {
        Torihikisaki t = tService.get(asKey("torihikisakiRef"));
        if(!loginYago.equals(t.getYagoRef().getModel())) return false;
        Hinmoku h = hService.get(asKey("hinmokuRef"));
        if(!loginYago.equals(h.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaEdit();
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            return forward("create");
        }
        
        ShukkaKeitai shukkaKeitai = new ShukkaKeitai();
        shukkaKeitai.getTorihikisakiRef().setModel(tService.get(asKey("torihikisakiRef")));
        shukkaKeitai.getHinmokuRef().setModel(hService.get(asKey("hinmokuRef")));
        shukkaKeitai.getYagoRef().setModel(loginYago);
        shukkaKeitai.setJuryo(asInteger("juryo"));
        shukkaKeitai.setPrice(asInteger("price"));
        shukkaKeitai.setHoso(asString("hoso"));
        service.insert(shukkaKeitai);
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add(meta.juryo, v.required(), v.maxlength(6), v.integerType());
        v.add(meta.price, v.required(), v.maxlength(6), v.integerType());
        v.add(meta.hoso, v.required(), v.maxlength(10));
        return v.validate();
    }

}
