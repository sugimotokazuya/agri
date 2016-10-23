package agri.controller.kintai.shift;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.model.kintai.Shift;
import agri.service.Const;
import agri.service.kintai.ShiftService;

import com.google.appengine.api.datastore.Key;

public class UpdateController extends BaseController {

    private ShiftService shiftService = new ShiftService();
    
    @Override
    public boolean validateAuth() {
        if(loginUser.isAuthKintai()) return true;
        Key key = asKey("key");
        Long version = asLong("version");
        Shift shift = shiftService.get(key, version);
        if(!loginUser.equals(shift.getUserRef().getModel())) return false;
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("./");
        }
        Key key = asKey("key");
        Long version = asLong("version");
        int yasumi = asInteger("yasumi");
        Shift shift = shiftService.get(key, version);
        shift.setYasumi(yasumi);
        shiftService.update(shift);
        
        return redirect("/kintai/shift");
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("version", v.required(), v.longType());
        v.add("yasumi", v.required(), v.doubleRange(0, 3));
        return v.validate();
    }
}
