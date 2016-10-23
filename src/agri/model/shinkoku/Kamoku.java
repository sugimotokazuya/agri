package agri.model.shinkoku;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.shinkoku.TekiyoMeta;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Kamoku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    /**
     * 1:資産,2:負債,3:収入,4:支出,5:開始残高
     */
    private int kubun;
    
    private String name;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }
    
    @Attribute(persistent = false)
    private InverseModelListRef<Tekiyo, Kamoku> tekiyoListRef = 
        new InverseModelListRef<Tekiyo, Kamoku>(Tekiyo.class, TekiyoMeta.get().kamokuRef.getName()
                , this, new Sort(TekiyoMeta.get().name.getName(), SortDirection.ASCENDING));

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
        Kamoku other = (Kamoku) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setKubun(int kubun) {
        this.kubun = kubun;
    }

    public int getKubun() {
        return kubun;
    }
    
    public String getKubunName() {
        switch(kubun)
        {
            case 1: return "資産";
            case 2: return "負債";
            case 3: return "収入";
            case 4: return "支出";
            case 5: return "開始残高";
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public InverseModelListRef<Tekiyo, Kamoku> getTekiyoListRef() {
        return tekiyoListRef;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }
}
