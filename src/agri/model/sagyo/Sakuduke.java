package agri.model.sagyo;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.SagyoMeta;
import agri.model.Hatake;
import agri.model.User;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Sakuduke implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private Date startDate;
    private double area;
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private ModelRef<Hatake> hatakeRef = new ModelRef<Hatake>(Hatake.class);
    private ModelRef<User> tantoRef = new ModelRef<User>(User.class);
    private ModelRef<User> adminUserRef = new ModelRef<User>(User.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<Sagyo, Sakuduke> sagyoListRef = 
        new InverseModelListRef<Sagyo, Sakuduke>(Sagyo.class, SagyoMeta.get().sakudukeRef.getName()
                , this, new Sort(SagyoMeta.get().date.getName(), SortDirection.ASCENDING));
    
    // 0:実行中, 1:完了
    private int status;
    
    // 0:利用しない,1:利用する
    private int easyInput;

    public String getStatusStr() {
        switch(status) {
        case 0: return "進行中";
        case 1: return "完了済";
        }
        return "";
    }
    
    public String getEasyInputStr() {
        switch(easyInput) {
        case 0: return "利用しない";
        case 1: return "利用する";
        }
        return "";
    }

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
        Sakuduke other = (Sakuduke) obj;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ModelRef<Hatake> getHatakeRef() {
        return hatakeRef;
    }

    public ModelRef<User> getTantoRef() {
        return tantoRef;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public InverseModelListRef<Sagyo, Sakuduke> getSagyoListRef() {
        return sagyoListRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public ModelRef<User> getAdminUserRef() {
        return adminUserRef;
    }

    public int getEasyInput() {
        return easyInput;
    }

    public void setEasyInput(int easyInput) {
        this.easyInput = easyInput;
    }

}
