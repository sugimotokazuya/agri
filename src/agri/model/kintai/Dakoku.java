package agri.model.kintai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.User;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Dakoku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<User> userRef = new ModelRef<User>(User.class);
    private Date date;
    private Date start;
    private Date end;
    private Date out;
    private Date in;
    private boolean yasumi;
    private boolean ohiru = true;
    private boolean zangyo;
    
    public int getStartInt() {
        return Util.createTimeInt(start);
    }
    
    public int getEndInt() {
        return Util.createTimeInt(end);
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
        Dakoku other = (Dakoku) obj;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getOut() {
        return out;
    }

    public void setOut(Date out) {
        this.out = out;
    }

    public Date getIn() {
        return in;
    }

    public void setIn(Date in) {
        this.in = in;
    }

    public boolean isYasumi() {
        return yasumi;
    }

    public void setYasumi(boolean yasumi) {
        this.yasumi = yasumi;
    }

    public boolean isZangyo() {
        return zangyo;
    }

    public void setZangyo(boolean zangyo) {
        this.zangyo = zangyo;
    }

    public boolean isOhiru() {
        return ohiru;
    }

    public void setOhiru(boolean ohiru) {
        this.ohiru = ohiru;
    }

}
