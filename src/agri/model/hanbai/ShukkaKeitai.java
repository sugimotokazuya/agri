package agri.model.hanbai;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class ShukkaKeitai implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<Torihikisaki> torihikisakiRef = new ModelRef<Torihikisaki>(Torihikisaki.class);
    private ModelRef<Hinmoku> hinmokuRef = new ModelRef<Hinmoku>(Hinmoku.class);
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    private String hoso;
    private int juryo;
    private int price;
    private int delete;
    
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
        ShukkaKeitai other = (ShukkaKeitai) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public ModelRef<Torihikisaki> getTorihikisakiRef() {
        return torihikisakiRef;
    }

    public ModelRef<Hinmoku> getHinmokuRef() {
        return hinmokuRef;
    }


    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public String getHoso() {
        return hoso;
    }

    public void setHoso(String hoso) {
        this.hoso = hoso;
    }

    public int getJuryo() {
        return juryo;
    }

    public void setJuryo(int juryo) {
        this.juryo = juryo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
