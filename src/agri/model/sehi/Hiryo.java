package agri.model.sehi;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sehi.SekkeiMeta;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Hiryo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private int nkeisu;
    private double seibunN;
    private double seibunP;
    private double seibunK;
    private double seibunCa;
    private double seibunMg;
    private double seibunB;
    private double seibunMn;
    private double seibunFe;
    private int weight;
    private boolean delete;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<Sekkei, Hiryo> sekkeiListRef = 
        new InverseModelListRef<Sekkei, Hiryo>(Sekkei.class, SekkeiMeta.get().hiryoRef.getName()
                , this, new Sort(SekkeiMeta.get().sehiryo.getName(), SortDirection.ASCENDING));

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
        Hiryo other = (Hiryo) obj;
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

    public double getSeibunN() {
        return seibunN;
    }

    public void setSeibunN(double seibunN) {
        this.seibunN = seibunN;
    }

    public double getSeibunP() {
        return seibunP;
    }

    public void setSeibunP(double seibunP) {
        this.seibunP = seibunP;
    }

    public double getSeibunK() {
        return seibunK;
    }

    public void setSeibunK(double seibunK) {
        this.seibunK = seibunK;
    }

    public double getSeibunCa() {
        return seibunCa;
    }

    public void setSeibunCa(double seibunCa) {
        this.seibunCa = seibunCa;
    }

    public double getSeibunMg() {
        return seibunMg;
    }

    public void setSeibunMg(double seibunMg) {
        this.seibunMg = seibunMg;
    }

    public double getSeibunB() {
        return seibunB;
    }

    public void setSeibunB(double seibunB) {
        this.seibunB = seibunB;
    }

    public double getSeibunMn() {
        return seibunMn;
    }

    public void setSeibunMn(double seibunMn) {
        this.seibunMn = seibunMn;
    }

    public double getSeibunFe() {
        return seibunFe;
    }

    public void setSeibunFe(double seibunFe) {
        this.seibunFe = seibunFe;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getNkeisu() {
        return nkeisu;
    }

    public void setNkeisu(int nkeisu) {
        this.nkeisu = nkeisu;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public InverseModelListRef<Sekkei, Hiryo> getSekkeiListRef() {
        return sekkeiListRef;
    }
}
