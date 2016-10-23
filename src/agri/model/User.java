package agri.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.SagyoUserMeta;
import agri.meta.sagyo.SakudukeMeta;
import agri.model.sagyo.SagyoUser;
import agri.model.sagyo.Sakuduke;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private Email email;
    private Email email2;
    private int employeeType; // 0:アルバイト,1:正規社員
    private boolean kakudukeTanto;
    private boolean delete;
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    private boolean useDakoku;
    
    // 権限
    private boolean authUserView;
    private boolean authUserEdit;
    private boolean authSagyoItemView;
    private boolean authSagyoItemEdit;
    private boolean authSakudukeView;
    private boolean authSakudukeEdit;
    private boolean authSagyoEdit;
    private boolean authSehiView;
    private boolean authSehiEdit;
    private boolean authHatakeView;
    private boolean authHatakeEdit;
    private boolean authMailView;
    private boolean authMailEdit;
    private boolean authTorihikisakiView;
    private boolean authTorihikisakiEdit;
    private boolean authShukkaView;
    private boolean authShukkaEdit;
    private boolean authSeikyuView;
    private boolean authSeikyuEdit;
    private boolean authHinmokuView;
    private boolean authHinmokuEdit;
    private boolean authShinkoku;
    private boolean authKintai;
    
    @Attribute(persistent = false)
    private InverseModelListRef<SagyoUser, User> sagyoUserListRef = 
        new InverseModelListRef<SagyoUser, User>(SagyoUser.class, SagyoUserMeta.get().userRef.getName()
                , this, new Sort(SagyoUserMeta.get().minutes.getName(), SortDirection.DESCENDING));
    
    @Attribute(persistent = false)
    private InverseModelListRef<Sakuduke, User> sakudukeListRef = 
        new InverseModelListRef<Sakuduke, User>(Sakuduke.class, SakudukeMeta.get().tantoRef.getName()
                , this, new Sort(SakudukeMeta.get().name.getName(), SortDirection.ASCENDING));
    
    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Email getEmail() {
        return email;
    }
    
    public String getEmailStr() {
        if(email == null) return null;
        return email.getEmail();
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public InverseModelListRef<SagyoUser, User> getSagyoUserListRef() {
        return sagyoUserListRef;
    }

    public InverseModelListRef<Sakuduke, User> getSakudukeListRef() {
        return sakudukeListRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public boolean isAuthUserView() {
        return authUserView;
    }

    public void setAuthUserView(boolean authUserView) {
        this.authUserView = authUserView;
    }

    public boolean isAuthUserEdit() {
        return authUserEdit;
    }

    public void setAuthUserEdit(boolean authUserEdit) {
        this.authUserEdit = authUserEdit;
    }

    public boolean isAuthSagyoItemView() {
        return authSagyoItemView;
    }

    public void setAuthSagyoItemView(boolean authSagyoItemView) {
        this.authSagyoItemView = authSagyoItemView;
    }

    public boolean isAuthSagyoItemEdit() {
        return authSagyoItemEdit;
    }

    public void setAuthSagyoItemEdit(boolean authSagyoItemEdit) {
        this.authSagyoItemEdit = authSagyoItemEdit;
    }

    public boolean isAuthSakudukeView() {
        return authSakudukeView;
    }

    public void setAuthSakudukeView(boolean authSakudukeView) {
        this.authSakudukeView = authSakudukeView;
    }

    public boolean isAuthSakudukeEdit() {
        return authSakudukeEdit;
    }

    public void setAuthSakudukeEdit(boolean authSakudukeEdit) {
        this.authSakudukeEdit = authSakudukeEdit;
    }

    public boolean isAuthSagyoEdit() {
        return authSagyoEdit;
    }

    public void setAuthSagyoEdit(boolean authSagyoEdit) {
        this.authSagyoEdit = authSagyoEdit;
    }

    public boolean isAuthSehiView() {
        return authSehiView;
    }

    public void setAuthSehiView(boolean authSehiView) {
        this.authSehiView = authSehiView;
    }

    public boolean isAuthSehiEdit() {
        return authSehiEdit;
    }

    public void setAuthSehiEdit(boolean authSehiEdit) {
        this.authSehiEdit = authSehiEdit;
    }

    public boolean isAuthHatakeView() {
        return authHatakeView;
    }

    public void setAuthHatakeView(boolean authHatakeView) {
        this.authHatakeView = authHatakeView;
    }

    public boolean isAuthHatakeEdit() {
        return authHatakeEdit;
    }

    public void setAuthHatakeEdit(boolean authHatakeEdit) {
        this.authHatakeEdit = authHatakeEdit;
    }

    public boolean isAuthMailView() {
        return authMailView;
    }

    public void setAuthMailView(boolean authMailView) {
        this.authMailView = authMailView;
    }

    public boolean isAuthMailEdit() {
        return authMailEdit;
    }

    public void setAuthMailEdit(boolean authMailEdit) {
        this.authMailEdit = authMailEdit;
    }

    public boolean isAuthTorihikisakiView() {
        return authTorihikisakiView;
    }

    public void setAuthTorihikisakiView(boolean authTorihikisakiView) {
        this.authTorihikisakiView = authTorihikisakiView;
    }

    public boolean isAuthTorihikisakiEdit() {
        return authTorihikisakiEdit;
    }

    public void setAuthTorihikisakiEdit(boolean authTorihikisakiEdit) {
        this.authTorihikisakiEdit = authTorihikisakiEdit;
    }

    public boolean isAuthShukkaView() {
        return authShukkaView;
    }

    public void setAuthShukkaView(boolean authShukkaView) {
        this.authShukkaView = authShukkaView;
    }

    public boolean isAuthShukkaEdit() {
        return authShukkaEdit;
    }

    public void setAuthShukkaEdit(boolean authShukkaEdit) {
        this.authShukkaEdit = authShukkaEdit;
    }

    public boolean isAuthSeikyuView() {
        return authSeikyuView;
    }

    public void setAuthSeikyuView(boolean authSeikyuView) {
        this.authSeikyuView = authSeikyuView;
    }

    public boolean isAuthSeikyuEdit() {
        return authSeikyuEdit;
    }

    public void setAuthSeikyuEdit(boolean authSeikyuEdit) {
        this.authSeikyuEdit = authSeikyuEdit;
    }

    public boolean isAuthHinmokuView() {
        return authHinmokuView;
    }

    public void setAuthHinmokuView(boolean authHinmokuView) {
        this.authHinmokuView = authHinmokuView;
    }

    public boolean isAuthHinmokuEdit() {
        return authHinmokuEdit;
    }

    public void setAuthHinmokuEdit(boolean authHinmokuEdit) {
        this.authHinmokuEdit = authHinmokuEdit;
    }

    public boolean isAuthShinkoku() {
        return authShinkoku;
    }

    public void setAuthShinkoku(boolean authShinkoku) {
        this.authShinkoku = authShinkoku;
    }

    public Email getEmail2() {
        return email2;
    }

    public void setEmail2(Email email2) {
        this.email2 = email2;
    }
    
    public String getEmail2Str() {
        if(email2 == null) return null;
        return email2.getEmail();
    }

    public boolean isUseDakoku() {
        return useDakoku;
    }

    public void setUseDakoku(boolean useDakoku) {
        this.useDakoku = useDakoku;
    }

    public boolean isAuthKintai() {
        return authKintai;
    }

    public void setAuthKintai(boolean authKintai) {
        this.authKintai = authKintai;
    }

    public boolean isKakudukeTanto() {
        return kakudukeTanto;
    }

    public void setKakudukeTanto(boolean kakudukeTanto) {
        this.kakudukeTanto = kakudukeTanto;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }
}
