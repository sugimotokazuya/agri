package agri.model.hanbai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.hanbai.ShukkaRecMeta;
import agri.model.Torihikisaki;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Shukka implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private Date date;
    private ModelRef<Torihikisaki> torihikisakiRef = new ModelRef<Torihikisaki>(Torihikisaki.class);
    private String torihikisakiName;    // 取引先名称が変わった時、削除された時に旧名を表示するため
    // 0:直売, 1:請求前, 2:請求済み, 
    private int kaishu;
    private int soryo;
    private String okurisaki;
    private String biko;
    private int tax;
    private int kakudukeMaisu;
    private int kuchisu;
    
    @Attribute(persistent = false)
    private InverseModelListRef<ShukkaRec, Shukka> recListRef = 
        new InverseModelListRef<ShukkaRec, Shukka>(ShukkaRec.class, ShukkaRecMeta.get().shukkaRef.getName()
                , this, new Sort(ShukkaRecMeta.get().suryo.getName(), SortDirection.ASCENDING));
    
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);

    public String getKaishuStr() {
        switch(kaishu) {
        case 0: return "直売";
        case 1: return "請求前";
        case 2: return "請求済";
        }
        return null;
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
        Shukka other = (Shukka) obj;
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

    public int getKaishu() {
        return kaishu;
    }

    public void setKaishu(int kaishu) {
        this.kaishu = kaishu;
    }

    public int getSoryo() {
        return soryo;
    }

    public void setSoryo(int soryo) {
        this.soryo = soryo;
    }

    public ModelRef<Torihikisaki> getTorihikisakiRef() {
        return torihikisakiRef;
    }

    public String getOkurisaki() {
        return okurisaki;
    }

    public void setOkurisaki(String okurisaki) {
        this.okurisaki = okurisaki;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public int getKakudukeMaisu() {
        return kakudukeMaisu;
    }

    public void setKakudukeMaisu(int kakudukeMaisu) {
        this.kakudukeMaisu = kakudukeMaisu;
    }

    public InverseModelListRef<ShukkaRec, Shukka> getRecListRef() {
        return recListRef;
    }

    public String getTorihikisakiName() {
        return torihikisakiName;
    }

    public void setTorihikisakiName(String torihikisakiName) {
        this.torihikisakiName = torihikisakiName;
    }

    public int getKuchisu() {
        return kuchisu;
    }

    public void setKuchisu(int kuchisu) {
        this.kuchisu = kuchisu;
    }
}
