package agri.model.hanbai;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Chokubai;
import agri.model.mail.OneNotice;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class UriageKingaku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<OneNotice> oneNoticeRef = new ModelRef<OneNotice>(OneNotice.class);
    private ModelRef<Chokubai> chokubaiRef = new ModelRef<Chokubai>(Chokubai.class);
    
    private int kingaku;
    
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
        UriageKingaku other = (UriageKingaku) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public int getKingaku() {
        return kingaku;
    }

    public void setKingaku(int kingaku) {
        this.kingaku = kingaku;
    }

    public ModelRef<OneNotice> getOneNoticeRef() {
        return oneNoticeRef;
    }

    public ModelRef<Chokubai> getChokubaiRef() {
        return chokubaiRef;
    }
}
