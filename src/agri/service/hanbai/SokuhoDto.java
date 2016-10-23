package agri.service.hanbai;

import java.util.HashMap;

import agri.model.hanbai.ShukkaKingaku;
import agri.model.hanbai.UriageKingaku;

import com.google.appengine.api.datastore.Key;

public class SokuhoDto {

    private UriageKingaku uk;
    private ShukkaKingaku sk;
    
    // Keyは品目
    private HashMap<Key, Integer> ucMap = new HashMap<Key, Integer>();
    private HashMap<Key, Integer> scMap = new HashMap<Key, Integer>();
    
    public SokuhoDto() {
        
    }
    
    public UriageKingaku getUk() {
        return uk;
    }
    public void setUk(UriageKingaku uk) {
        this.uk = uk;
    }
    public HashMap<Key, Integer> getUcMap() {
        return ucMap;
    }
    public ShukkaKingaku getSk() {
        return sk;
    }
    public void setSk(ShukkaKingaku sk) {
        this.sk = sk;
    }
    public HashMap<Key, Integer> getScMap() {
        return scMap;
    }
}
