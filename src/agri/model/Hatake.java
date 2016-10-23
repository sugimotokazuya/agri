package agri.model;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.sagyo.SakudukeMeta;
import agri.meta.sehi.SokuteiMeta;
import agri.model.sagyo.Sakuduke;
import agri.model.sehi.Sokutei;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Hatake implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    private String shortName;
    private int no;
    private String address;
    private Date startYuhki;
    private double area;
    private boolean delete;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<Sakuduke, Hatake> sakudukeListRef = 
        new InverseModelListRef<Sakuduke, Hatake>(Sakuduke.class, SakudukeMeta.get().hatakeRef.getName()
                , this, new Sort(SakudukeMeta.get().name.getName(), SortDirection.ASCENDING));

    @Attribute(persistent = false)
    private InverseModelListRef<Sokutei, Hatake> sokuteiListRef = 
        new InverseModelListRef<Sokutei, Hatake>(Sokutei.class, SokuteiMeta.get().hatakeRef.getName()
                , this, new Sort(SokuteiMeta.get().date.getName(), SortDirection.DESCENDING));
    
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
        Hatake other = (Hatake) obj;
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

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public InverseModelListRef<Sakuduke, Hatake> getSakudukeListRef() {
        return sakudukeListRef;
    }

    public InverseModelListRef<Sokutei, Hatake> getSokuteiListRef() {
        return sokuteiListRef;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getStartYuhki() {
        return startYuhki;
    }

    public void setStartYuhki(Date startYuhki) {
        this.startYuhki = startYuhki;
    }
}
