package agri.model.hanbai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.User;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Kakuduke implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private ModelRef<Shukka> shukkaRef = new ModelRef<Shukka>(Shukka.class);
    private ModelRef<ShukkaRec> shukkaRecRef = new ModelRef<ShukkaRec>(ShukkaRec.class);
    private ModelRef<User> userRef = new ModelRef<User>(User.class);
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private int plus;
    private Date date;
    private String biko;
    

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
        Kakuduke other = (Kakuduke) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public ModelRef<Shukka> getShukkaRef() {
        return shukkaRef;
    }

    public int getPlus() {
        return plus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public ModelRef<ShukkaRec> getShukkaRecRef() {
        return shukkaRecRef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ModelRef<User> getUserRef() {
        return userRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

}
