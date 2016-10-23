package agri.model.kintai;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import agri.model.User;
import agri.model.Yago;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Kinmuhyo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private ModelRef<Yago> yagoRef = new ModelRef<Yago>(Yago.class);
    private ModelRef<User> userRef = new ModelRef<User>(User.class);
    private Date date;
    
    private int employeeType; // 0:アルバイト,1:正社員
    
    // 0:申請前,1:申請中,2:承認,3:却下
    private int shonin;
    
    private String riyu;
    
    private int yasumiZengetsuZan;
    private int yasumiZan;
    private int yasumiPlus;
    private int yasumiHour;
    private int kinmuDays;
    private int kinmuHours;
    private int zangyoMinutes;
    
    public String getShoninStr() {
        switch(shonin) {
        case 0: return "申請前";
        case 1: return "申請中";
        case 2: return "承認済";
        case 3: return "却下";
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
        Kinmuhyo other = (Kinmuhyo) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public ModelRef<User> getUserRef() {
        return userRef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ModelRef<Yago> getYagoRef() {
        return yagoRef;
    }

    public int getShonin() {
        return shonin;
    }

    public void setShonin(int shonin) {
        this.shonin = shonin;
    }

    public String getRiyu() {
        return riyu;
    }

    public void setRiyu(String riyu) {
        this.riyu = riyu;
    }

    public int getYasumiZengetsuZan() {
        return yasumiZengetsuZan;
    }

    public void setYasumiZengetsuZan(int yasumiZengetsuZan) {
        this.yasumiZengetsuZan = yasumiZengetsuZan;
    }

    public int getYasumiZan() {
        return yasumiZan;
    }

    public void setYasumiZan(int yasumiZan) {
        this.yasumiZan = yasumiZan;
    }

    public int getYasumiPlus() {
        return yasumiPlus;
    }

    public void setYasumiPlus(int yasumiPlus) {
        this.yasumiPlus = yasumiPlus;
    }

    public int getYasumiHour() {
        return yasumiHour;
    }

    public void setYasumiHour(int yasumiHour) {
        this.yasumiHour = yasumiHour;
    }

    public int getKinmuDays() {
        return kinmuDays;
    }

    public void setKinmuDays(int kinmuDays) {
        this.kinmuDays = kinmuDays;
    }

    public int getKinmuHours() {
        return kinmuHours;
    }

    public void setKinmuHours(int kinmuHours) {
        this.kinmuHours = kinmuHours;
    }

    public int getZangyoMinutes() {
        return zangyoMinutes;
    }

    public void setZangyoMinutes(int zangyoMinutes) {
        this.zangyoMinutes = zangyoMinutes;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

}
