package agri.model.hanbai;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Hinmoku;

import com.google.appengine.api.datastore.Key;

/**
 * 直売店舗と品目ごとの出荷数量
 * @author kazuya
 *
 */
@Model(schemaVersion = 1)
public class ShukkaCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private ModelRef<ShukkaKingaku> shukkaKingakuRef = new ModelRef<ShukkaKingaku>(ShukkaKingaku.class);
    private ModelRef<Hinmoku> hinmokuRef = new ModelRef<Hinmoku>(Hinmoku.class);
    //マスタにする必要ある？
    
    private int count;

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
        ShukkaCount other = (ShukkaCount) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ModelRef<ShukkaKingaku> getShukkaKingakuRef() {
        return shukkaKingakuRef;
    }

    public ModelRef<Hinmoku> getHinmokuRef() {
        return hinmokuRef;
    }
}