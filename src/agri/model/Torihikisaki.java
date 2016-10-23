package agri.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.Sort;

import agri.meta.hanbai.OkurisakiMeta;
import agri.model.hanbai.Okurisaki;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

@Model(schemaVersion = 1)
public class Torihikisaki implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String name;
    
    private boolean delete;
    
    private String biko;
    
    // 0:納品書なし、1:納品書有り、2:納品書金額あり,3:納品書兼請求書, 4:納品書有り（宛名は送り先ではなく取引先）,5:納品書金額有り（宛名は送り先ではなく取引先）
    private int nohin = 0;
    
    // 0:一括プリントする, 1:一括プリントしない
    private int ikkatsuPrint = 0;
    
    // 0:直売,1:その他
    private int status = 0;
    
    private String address1;
    private String address2;
    private String address3;
    private String yubin1;
    private String yubin2;
    private String tel;
    private int haitatsubi; // 何日後着か
    private int kiboujikan; //0:午前中,1:12時,2:14時,3:16時,4:18時,5:20時,6:希望しない
    private int soryo;
    
    // 依頼主
    private String iraiName;
    private String iraiAddress1;
    private String iraiAddress2;
    private String iraiYubin1;
    private String iraiYubin2;
    private String iraiTel;
    
    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    
    @Attribute(persistent = false)
    private InverseModelListRef<Okurisaki, Torihikisaki> okurisakiListRef = 
        new InverseModelListRef<Okurisaki, Torihikisaki>(Okurisaki.class, OkurisakiMeta.get().torihikisakiRef.getName()
                , this, new Sort(OkurisakiMeta.get().name.getName(), SortDirection.ASCENDING));

    
    public String getNohinName() {
        switch(nohin) {
        case 0: return "納品書なし";
        case 1: return "納品書有り";
        case 2: return "納品書金額有り";
        case 3: return "納品書兼請求書";
        case 4: return "納品書有り（宛名は取引先名）";
        case 5: return "納品書金額有り（宛名は取引先名）";
        }
        return null;
    }
    
    public String getIkkatsuPrintStr() {
        switch(ikkatsuPrint) {
        case 0: return "する";
        case 1: return "しない";
        }
        return null;
    }
    
    public String getStatusName() {
        switch(status) {
            case 0: return "直売";
            case 1: return "その他";
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
        Torihikisaki other = (Torihikisaki) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public int getNohin() {
        return nohin;
    }

    public void setNohin(int nohin) {
        this.nohin = nohin;
    }

    public int getIkkatsuPrint() {
        return ikkatsuPrint;
    }

    public void setIkkatsuPrint(int ikkatsuPrint) {
        this.ikkatsuPrint = ikkatsuPrint;
    }

    public InverseModelListRef<Okurisaki, Torihikisaki> getOkurisakiListRef() {
        return okurisakiListRef;
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

    public int getSoryo() {
        return soryo;
    }

    public void setSoryo(int soryo) {
        this.soryo = soryo;
    }
}
