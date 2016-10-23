package agri.model.shinkoku;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Shiwake implements Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private int nendo;
    
    private Date hiduke;
    private ModelRef<Kamoku> karikataKamokuRef = new ModelRef<Kamoku>(Kamoku.class);
    private int karikataKingaku;
    private ModelRef<Kamoku> kashikataKamokuRef = new ModelRef<Kamoku>(Kamoku.class);
    private int kashikataKingaku;
    private ModelRef<Tekiyo> tekiyoRef = new ModelRef<Tekiyo>(Tekiyo.class);
    private String tekiyoText;
    private boolean totalUp = false;
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);

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
        Shiwake other = (Shiwake) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setHiduke(Date hiduke) {
        this.hiduke = hiduke;
    }

    public Date getHiduke() {
        return hiduke;
    }

    public ModelRef<Kamoku> getKarikataKamokuRef() {
        return karikataKamokuRef;
    }

    public void setKarikataKingaku(int karikataKingaku) {
        this.karikataKingaku = karikataKingaku;
    }

    public int getKarikataKingaku() {
        return karikataKingaku;
    }

    public ModelRef<Kamoku> getKashikataKamokuRef() {
        return kashikataKamokuRef;
    }

    public void setKashikataKingaku(int kashikataKingaku) {
        this.kashikataKingaku = kashikataKingaku;
    }

    public int getKashikataKingaku() {
        return kashikataKingaku;
    }

    public ModelRef<Tekiyo> getTekiyoRef() {
        return tekiyoRef;
    }

    public void setTekiyoText(String tekiyoText) {
        this.tekiyoText = tekiyoText;
    }

    public String getTekiyoText() {
        return tekiyoText;
    }

    public void setTotalUp(boolean totalUp) {
        this.totalUp = totalUp;
    }

    public boolean isTotalUp() {
        return totalUp;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public int getNendo() {
        return nendo;
    }

    public void setNendo(int nendo) {
        this.nendo = nendo;
    }

}
