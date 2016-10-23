package agri.controller.yago;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

import agri.controller.BaseController;
import agri.meta.YagoMeta;
import agri.model.User;
import agri.model.Yago;
import agri.service.Const;
import agri.service.UserService;
import agri.service.YagoService;

public class InsertController extends BaseController {

    private YagoMeta meta = YagoMeta.get();
    private YagoService service = new YagoService();
    private UserService userService = new UserService();
    
    @Override
    protected boolean validateAuth() {
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        if (!validate()) {
            requestScope("userList", userService.getAll());
            return forward("create");
        }
        Yago yago = new Yago();
        yago.setName(asString("name"));
        yago.setShonendo(asInteger("shonendo"));
        yago.setStartMonth(asInteger("startMonth"));
        yago.getOwnerRef().setKey(asKey("owner"));
        yago.setToiawase("〒000-0000\r\n高知県◯◯◯\r\n電話：000-000-0000\r\n担当者：◯◯◯◯");
        yago.setFurikomisaki1("ゆうちょ銀行電信振替\r\n記号-番号: 00000-0000000\r\n名義: ◯◯◯◯");
        yago.setFurikomisaki2("◯◯銀行　　◯◯支店\r\n口座番号: （普）0000000\r\n名義: ◯◯◯◯");
        service.insert(yago);
        
        // オーナーになったユーザーはUserの屋号もオーナーになった屋号に変更する
        // 権限をリセットしてユーザー閲覧と追加・変更権限を持たせる
        User user = yago.getOwnerRef().getModel();
        user.getYagoRef().setModel(yago);
        user.setAuthUserView(true);
        user.setAuthUserEdit(true);
        user.setAuthSagyoItemView(false);
        user.setAuthSagyoItemEdit(false);
        user.setAuthSakudukeView(false);
        user.setAuthSakudukeEdit(false);
        user.setAuthSagyoEdit(false);
        user.setAuthSehiView(false);
        user.setAuthSehiEdit(false);
        user.setAuthHatakeView(false);
        user.setAuthHatakeEdit(false);
        user.setAuthMailView(false);
        user.setAuthMailEdit(false);
        user.setAuthTorihikisakiView(false);
        user.setAuthTorihikisakiEdit(false);
        user.setAuthShukkaView(false);
        user.setAuthShukkaEdit(false);
        user.setAuthSeikyuView(false);
        user.setAuthSeikyuEdit(false);
        user.setAuthHinmokuView(false);
        user.setAuthHinmokuEdit(false);
        user.setAuthShinkoku(false);
        userService.update(user);
        
        return redirect(basePath);
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add(meta.name, v.required(), v.maxlength(20));
        v.add("owner", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add(meta.shonendo, v.required(), v.minlength(4), v.maxlength(4),v.integerType());
        v.add(meta.startMonth, v.required(), v.maxlength(2),v.integerType(), v.doubleRange(1, 12));
        return v.validate();
    }
}
