package agri.model.sehi;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Hatake;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Sokutei implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private Date date;
    private ModelRef<Hatake> hatakeRef = new ModelRef<Hatake>(Hatake.class);
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private String hinmoku;
    private double phH2o;
    private double phKcl;
    private double seibunNh4n;
    private double seibunNo3n;
    private int seibunP;
    private int seibunK;
    private int seibunCa;
    private int seibunMg;
    private double seibunMn;
    private double seibunFe;
    private double seibunEnbun;
    
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
        Sokutei other = (Sokutei) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ModelRef<Hatake> getHatakeRef() {
        return hatakeRef;
    }
    
    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public String getHinmoku() {
        return hinmoku;
    }

    public void setHinmoku(String hinmoku) {
        this.hinmoku = hinmoku;
    }

    public double getPhH2o() {
        return phH2o;
    }

    public void setPhH2o(double phH2o) {
        this.phH2o = phH2o;
    }

    public double getPhKcl() {
        return phKcl;
    }

    public void setPhKcl(double phKcl) {
        this.phKcl = phKcl;
    }

    public double getSeibunNh4n() {
        return seibunNh4n;
    }

    public void setSeibunNh4n(double seibunNh4n) {
        this.seibunNh4n = seibunNh4n;
    }

    public double getSeibunNo3n() {
        return seibunNo3n;
    }

    public void setSeibunNo3n(double seibunNo3n) {
        this.seibunNo3n = seibunNo3n;
    }

    public int getSeibunP() {
        return seibunP;
    }

    public void setSeibunP(int seibunP) {
        this.seibunP = seibunP;
    }

    public int getSeibunK() {
        return seibunK;
    }

    public void setSeibunK(int seibunK) {
        this.seibunK = seibunK;
    }

    public int getSeibunCa() {
        return seibunCa;
    }

    public void setSeibunCa(int seibunCa) {
        this.seibunCa = seibunCa;
    }

    public int getSeibunMg() {
        return seibunMg;
    }

    public void setSeibunMg(int seibunMg) {
        this.seibunMg = seibunMg;
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

    public double getSeibunEnbun() {
        return seibunEnbun;
    }

    public void setSeibunEnbun(double seibunEnbun) {
        this.seibunEnbun = seibunEnbun;
    }
}
