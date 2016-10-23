package agri.model.hanbai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Torihikisaki;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Seikyu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private Date date;
    private Date limitDate;
    private ModelRef<Torihikisaki> torihikisakiRef = new ModelRef<Torihikisaki>(Torihikisaki.class);
    private String torihikisakiName; // 変更・削除後の旧名称表示用
    private int seikyuKingaku;
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    // 0:未回収、1:回収済み
    private int kaishu;
    
    public String getKaishuStr() {
        switch(kaishu) {
        case 0: return "未回収";
        case 1: return "回収済";
        }
        return "";
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
        Seikyu other = (Seikyu) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public int getKaishu() {
        return kaishu;
    }

    public void setKaishu(int kaishu) {
        this.kaishu = kaishu;
    }

    public ModelRef<Torihikisaki> getTorihikisakiRef() {
        return torihikisakiRef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSeikyuKingaku() {
        return seikyuKingaku;
    }

    public void setSeikyuKingaku(int seikyuKingaku) {
        this.seikyuKingaku = seikyuKingaku;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public String getTorihikisakiName() {
        return torihikisakiName;
    }

    public void setTorihikisakiName(String torihikisakiName) {
        this.torihikisakiName = torihikisakiName;
    }
}
