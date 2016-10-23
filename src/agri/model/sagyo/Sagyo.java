package agri.model.sagyo;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.GazoMeta;
import agri.meta.sagyo.SagyoUserMeta;
import agri.meta.sagyo.ShiyoKikaiMeta;
import agri.meta.sagyo.ShiyoShizaiMeta;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Sagyo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    private Date date;
    private Double amount;
    
    @Attribute(lob = true)
    private String biko;
    private ModelRef<SagyoItem> sagyoItemRef = new ModelRef<SagyoItem>(SagyoItem.class);
    private ModelRef<Sakuduke> sakudukeRef = new ModelRef<Sakuduke>(Sakuduke.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<ShiyoShizai, Sagyo> shiyoShizaiListRef = 
        new InverseModelListRef<ShiyoShizai, Sagyo>(ShiyoShizai.class, ShiyoShizaiMeta.get().sagyoRef.getName()
                , this, new Sort(ShiyoShizaiMeta.get().amount.getName(), SortDirection.ASCENDING));
    
    @Attribute(persistent = false)
    private InverseModelListRef<ShiyoKikai, Sagyo> shiyoKikaiListRef = 
        new InverseModelListRef<ShiyoKikai, Sagyo>(ShiyoKikai.class, ShiyoKikaiMeta.get().sagyoRef.getName()
                , this, new Sort(ShiyoKikaiMeta.get().amount.getName(), SortDirection.ASCENDING));
    
    @Attribute(persistent = false)
    private InverseModelListRef<SagyoUser, Sagyo> sagyoUserListRef = 
        new InverseModelListRef<SagyoUser, Sagyo>(SagyoUser.class, SagyoUserMeta.get().sagyoRef.getName()
                , this, new Sort(SagyoUserMeta.get().userRef.getName(), SortDirection.ASCENDING));
    
    @Attribute(persistent = false)
    private InverseModelListRef<Gazo, Sagyo> gazoListRef = 
        new InverseModelListRef<Gazo, Sagyo>(Gazo.class, GazoMeta.get().sagyoRef.getName()
                , this, new Sort(GazoMeta.get().date.getName(), SortDirection.DESCENDING));
    
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
        Sagyo other = (Sagyo) obj;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public ModelRef<SagyoItem> getSagyoItemRef() {
        return sagyoItemRef;
    }

    public ModelRef<Sakuduke> getSakudukeRef() {
        return sakudukeRef;
    }

    public InverseModelListRef<ShiyoShizai, Sagyo> getShiyoShizaiListRef() {
        return shiyoShizaiListRef;
    }
    
    public InverseModelListRef<ShiyoKikai, Sagyo> getShiyoKikaiListRef() {
        return shiyoKikaiListRef;
    }
    
    public InverseModelListRef<SagyoUser, Sagyo> getSagyoUserListRef() {
        return sagyoUserListRef;
    }

    public InverseModelListRef<Gazo, Sagyo> getGazoListRef() {
        return gazoListRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

  
}
