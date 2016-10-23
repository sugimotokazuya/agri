package agri.controller.kintai;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.Dakoku;
import agri.model.kintai.ShuseiShinsei;
import agri.service.Const;
import agri.service.kintai.DakokuService;
import agri.service.kintai.ShuseiShinseiService;

public class ShoninUpdateController extends BaseController {

    private ShuseiShinseiService ssService = new ShuseiShinseiService();
    private DakokuService dakokuService = new DakokuService();

    @Override
    public Navigation run() throws Exception {

        if (!validate()) {
            return forward("shonin");
        }
        
        
        ShuseiShinsei ss = ssService.get(asKey("key"), asLong("version"));
        ss.setShonin(asInteger("shonin"));
        ssService.update(ss);
        
        if(ss.getShonin() == 2) {
            // 承認
            Dakoku dakoku = dakokuService.get(ss.getUserRef().getKey(), ss.getDate());
            boolean isNew = false;
            if(dakoku == null) {
                dakoku = new Dakoku();
                dakoku.getUserRef().setModel(ss.getUserRef().getModel());
                dakoku.setDate(ss.getDate());
                isNew = true;
            }
            switch(ss.getStatus()) {
            case 1: dakoku.setStart(ss.getTime());break;
            case 2: dakoku.setOut(ss.getTime());break;
            case 3: dakoku.setIn(ss.getTime());break;
            case 4: dakoku.setEnd(ss.getTime());break;
            case 5: dakoku.setZangyo(true);break;
            }
            if(isNew) {
                dakokuService.insert(dakoku);
            } else {
                dakokuService.update(dakoku);
            }
        }
        
        return redirect("shonin");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("shonin", v.required(), v.maxlength(1), v.integerType());
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        
        return v.validate();
    }
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthKintai();
    }
}
