package agri.model.sagyo;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.ShiyoShizaiMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Shizai implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private String nyushusaki;
    private String tanni;
    private String seizosha;
    private String beppyoName;
    private String beppyoKubun;
    private String riyu;
    private boolean delete;
    private ModelRef<SagyoItem> sagyoItemRef = new ModelRef<SagyoItem>(SagyoItem.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<ShiyoShizai, Shizai> shiyoShizaiListRef = 
        new InverseModelListRef<ShiyoShizai, Shizai>(ShiyoShizai.class, ShiyoShizaiMeta.get().sagyoRef.getName()
                , this, new Sort(ShiyoShizaiMeta.get().amount.getName(), SortDirection.ASCENDING));

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
        Shizai other = (Shizai) obj;
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

    public String getNyushusaki() {
        return nyushusaki;
    }

    public void setNyushusaki(String nyushusaki) {
        this.nyushusaki = nyushusaki;
    }

    public ModelRef<SagyoItem> getSagyoItemRef() {
        return sagyoItemRef;
    }

    public String getTanni() {
        return tanni;
    }

    public void setTanni(String tanni) {
        this.tanni = tanni;
    }

    public InverseModelListRef<ShiyoShizai, Shizai> getShiyoShizaiListRef() {
        return shiyoShizaiListRef;
    }

    public String getSeizosha() {
        return seizosha;
    }

    public void setSeizosha(String seizosha) {
        this.seizosha = seizosha;
    }

    public String getBeppyoName() {
        return beppyoName;
    }

    public void setBeppyoName(String beppyoName) {
        this.beppyoName = beppyoName;
    }

    public String getBeppyoKubun() {
        return beppyoKubun;
    }

    public void setBeppyoKubun(String beppyoKubun) {
        this.beppyoKubun = beppyoKubun;
    }

    public String getRiyu() {
        return riyu;
    }

    public void setRiyu(String riyu) {
        this.riyu = riyu;
    }

}
