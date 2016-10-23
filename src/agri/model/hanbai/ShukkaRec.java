package agri.model.hanbai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Hinmoku;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class ShukkaRec implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private ModelRef<Shukka> shukkaRef = new ModelRef<Shukka>(Shukka.class);
    private ModelRef<Hinmoku> hinmokuRef = new ModelRef<Hinmoku>(Hinmoku.class);
    private String hinmokuName; // 変更・削除後の旧名称表示用
    private int tanka;
    private double suryo;
    private Date date;
    private ModelRef<ShukkaKeitai> shukkaKeitaiRef = new ModelRef<ShukkaKeitai>(ShukkaKeitai.class);
    private String hosoName; // 変更・削除後の旧名称表示用
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
        ShukkaRec other = (ShukkaRec) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public int getTanka() {
        return tanka;
    }

    public void setTanka(int tanka) {
        this.tanka = tanka;
    }

    public double getSuryo() {
        return suryo;
    }

    public void setSuryo(double suryo) {
        this.suryo = suryo;
    }

    public ModelRef<Shukka> getShukkaRef() {
        return shukkaRef;
    }

    public ModelRef<Hinmoku> getHinmokuRef() {
        return hinmokuRef;
    }

    public ModelRef<ShukkaKeitai> getShukkaKeitaiRef() {
        return shukkaKeitaiRef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHinmokuName() {
        return hinmokuName;
    }

    public void setHinmokuName(String hinmokuName) {
        this.hinmokuName = hinmokuName;
    }

    public String getHosoName() {
        return hosoName;
    }

    public void setHosoName(String hosoName) {
        this.hosoName = hosoName;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }
}
