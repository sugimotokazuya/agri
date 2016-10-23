package agri.model.hanbai;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.Torihikisaki;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Okurisaki implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String yubin1;
    private String yubin2;
    private String tel;
    private int haitatsubi; // 何日後着か
    private int kiboujikan; //0:午前中,1:12時,2:14時,3:16時,4:18時,5:20時,6:希望しない
    private int soryo;
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private ModelRef<Torihikisaki> torihikisakiRef = new ModelRef<Torihikisaki>(Torihikisaki.class);
    
    // 依頼主
    private String iraiName;
    private String iraiAddress1;
    private String iraiAddress2;
    private String iraiYubin1;
    private String iraiYubin2;
    private String iraiTel;
    
    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    public String getKiboujikanStr() {
        switch(kiboujikan) {
        case 0: return "午前中";
        case 1: return "12時〜14時";
        case 2: return "14時〜16時";
        case 3: return "16時〜18時";
        case 4: return "18時〜20時";
        case 5: return "20時〜21時";
        case 6: return "希望しない";
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
        Okurisaki other = (Okurisaki) obj;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getYubin1() {
        return yubin1;
    }

    public void setYubin1(String yubin1) {
        this.yubin1 = yubin1;
    }

    public String getYubin2() {
        return yubin2;
    }

    public void setYubin2(String yubin2) {
        this.yubin2 = yubin2;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getHaitatsubi() {
        return haitatsubi;
    }

    public void setHaitatsubi(int haitatsubi) {
        this.haitatsubi = haitatsubi;
    }

    public int getKiboujikan() {
        return kiboujikan;
    }

    public void setKiboujikan(int kiboujikan) {
        this.kiboujikan = kiboujikan;
    }

    public ModelRef<Torihikisaki> getTorihikisakiRef() {
        return torihikisakiRef;
    }

    public int getSoryo() {
        return soryo;
    }

    public void setSoryo(int soryo) {
        this.soryo = soryo;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public String getIraiName() {
        return iraiName;
    }

    public void setIraiName(String iraiName) {
        this.iraiName = iraiName;
    }

    public String getIraiAddress1() {
        return iraiAddress1;
    }

    public void setIraiAddress1(String iraiAddress1) {
        this.iraiAddress1 = iraiAddress1;
    }

    public String getIraiAddress2() {
        return iraiAddress2;
    }

    public void setIraiAddress2(String iraiAddress2) {
        this.iraiAddress2 = iraiAddress2;
    }

    public String getIraiYubin1() {
        return iraiYubin1;
    }

    public void setIraiYubin1(String iraiYubin1) {
        this.iraiYubin1 = iraiYubin1;
    }

    public String getIraiYubin2() {
        return iraiYubin2;
    }

    public void setIraiYubin2(String iraiYubin2) {
        this.iraiYubin2 = iraiYubin2;
    }

    public String getIraiTel() {
        return iraiTel;
    }

    public void setIraiTel(String iraiTel) {
        this.iraiTel = iraiTel;
    }
}
