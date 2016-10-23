package agri.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.UserMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Yago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private boolean delete;
    private ModelRef<User> ownerRef = new ModelRef<User>(User.class);
    private ModelRef<User> kintaiRef = new ModelRef<User>(User.class);
    private int shonendo;
    private int startMonth;
    
    private String toiawase;
    private String furikomisaki1;
    private String furikomisaki2;
    private String furikomisaki3;
    
    @Attribute(persistent = false)
    private InverseModelListRef<User, Yago> userListRef = 
        new InverseModelListRef<User, Yago>(User.class, UserMeta.get().yagoRef.getName()
                , this, new Sort(UserMeta.get().name.getName(), SortDirection.ASCENDING));
    
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
        Yago other = (Yago) obj;
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public ModelRef<User> getOwnerRef() {
        return ownerRef;
    }

    public InverseModelListRef<User, Yago> getUserListRef() {
        return userListRef;
    }
    
    public List<User> getUserListWithOutDeleted() {
        List<User> list = getUserListRef().getModelList();
        Iterator<User> ite = list.iterator();
        while(ite.hasNext()) {
            User user = ite.next();
            if(user.isDelete()) ite.remove();
        }
        return list;
    }

    public String getToiawase() {
        return toiawase;
    }

    public void setToiawase(String toiawase) {
        this.toiawase = toiawase;
    }

    public String getFurikomisaki1() {
        return furikomisaki1;
    }

    public void setFurikomisaki1(String furikomisaki1) {
        this.furikomisaki1 = furikomisaki1;
    }

    public String getFurikomisaki2() {
        return furikomisaki2;
    }

    public void setFurikomisaki2(String furikomisaki2) {
        this.furikomisaki2 = furikomisaki2;
    }

    public String getFurikomisaki3() {
        return furikomisaki3;
    }

    public void setFurikomisaki3(String furikomisaki3) {
        this.furikomisaki3 = furikomisaki3;
    }

    public ModelRef<User> getKintaiRef() {
        return kintaiRef;
    }

    public int getShonendo() {
        return shonendo;
    }

    public void setShonendo(int shonendo) {
        this.shonendo = shonendo;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

}
