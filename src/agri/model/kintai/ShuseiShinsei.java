package agri.model.kintai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.User;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class ShuseiShinsei implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private ModelRef<User> userRef = new ModelRef<User>(User.class);
    private Date date;
    private Date beforeTime;
    private Date time;
    // 1:出勤,2:外出,3:戻り,4:退勤,5:残業
    private int status;
    private String riyu;
    
    // 0:申請中,1:却下,2:承認
    private int shonin;
    
    public String getStatusStr() {
        switch(status) {
        case 1: return "出勤";
        case 2: return "外出";
        case 3: return "戻り";
        case 4: return "退勤";
        case 5: return "残業";
        }
        return "";
    }
    
    public String getShoninStr() {
        switch(shonin) {
        case 0: return "申請中";
        case 1: return "却下";
        case 2: return "承認済";
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
        ShuseiShinsei other = (ShuseiShinsei) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public ModelRef<User> getUserRef() {
        return userRef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRiyu() {
        return riyu;
    }

    public void setRiyu(String riyu) {
        this.riyu = riyu;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShonin() {
        return shonin;
    }

    public void setShonin(int shonin) {
        this.shonin = shonin;
    }

    public Date getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(Date beforeTime) {
        this.beforeTime = beforeTime;
    }

}
