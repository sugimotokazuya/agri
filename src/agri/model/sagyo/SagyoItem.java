package agri.model.sagyo;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.KikaiMeta;
import agri.meta.sagyo.SagyoMeta;
import agri.meta.sagyo.ShizaiMeta;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class SagyoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private String tanni;
    private boolean delete;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);

    @Attribute(persistent = false)
    private InverseModelListRef<Sagyo, SagyoItem> sagyoListRef = 
        new InverseModelListRef<Sagyo, SagyoItem>(Sagyo.class, SagyoMeta.get().sagyoItemRef.getName()
                , this, new Sort(SagyoMeta.get().date.getName(), SortDirection.ASCENDING));
    
    @Attribute(persistent = false)
    private InverseModelListRef<Shizai, SagyoItem> shizaiListRef = 
        new InverseModelListRef<Shizai, SagyoItem>(Shizai.class, ShizaiMeta.get().sagyoItemRef.getName()
                , this, new Sort(ShizaiMeta.get().name.getName(), SortDirection.ASCENDING));

    @Attribute(persistent = false)
    private InverseModelListRef<Kikai, SagyoItem> kikaiListRef = 
        new InverseModelListRef<Kikai, SagyoItem>(Kikai.class, KikaiMeta.get().sagyoItemRef.getName()
                , this, new Sort(KikaiMeta.get().name.getName(), SortDirection.ASCENDING));
    
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
        SagyoItem other = (SagyoItem) obj;
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getTanni() {
        return tanni;
    }

    public void setTanni(String tanni) {
        this.tanni = tanni;
    }

    public InverseModelListRef<Shizai, SagyoItem> getShizaiListRef() {
        return shizaiListRef;
    }

    public InverseModelListRef<Kikai, SagyoItem> getKikaiListRef() {
        return kikaiListRef;
    }

    public InverseModelListRef<Sagyo, SagyoItem> getSagyoListRef() {
        return sagyoListRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }
}
