package agri.controller.hanbai.seikyu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Seikyu;
import agri.model.hanbai.SeikyuRec;
import agri.model.hanbai.ShukkaRec;
import agri.service.Util;
import agri.service.hanbai.SeikyuService;
import agri.service.hanbai.ShukkaService;
import agri.service.hanbai.ShukkaService.ShukkaDto;

import com.google.appengine.api.datastore.Key;

public class PrintController extends BaseController {

    private SeikyuService service = new SeikyuService();
    private ShukkaService shukkaService = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        Seikyu s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthSeikyuView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Seikyu s = service.get(asKey("key"), asLong("version"));
        List<SeikyuRec> list = service.getRecList(s.getKey());
        
        Key[] keys = new Key[list.size()];
        for(int i = 0; i < keys.length; i++) {
            keys[i] = list.get(i).getShukkaRef().getKey();
        }
        
        List<ShukkaDto> dtoList = shukkaService.getDtoList(keys);
        
        int sumKingaku = 0;
        int sumTax = 0;
        int sumSoryo = 0;
        int sumGokei = 0;
        
        Iterator<ShukkaDto> dtoIte = dtoList.iterator();
        while(dtoIte.hasNext()) {
            ShukkaDto dto = dtoIte.next();
            sumKingaku += dto.getShokei();
            sumTax += dto.getShukka().getTax();
            sumSoryo += dto.getShukka().getSoryo();
            sumGokei += dto.getGokei();
        }
        
        requestScope("sumKingaku", sumKingaku);
        requestScope("sumTax", sumTax);
        requestScope("sumSoryo", sumSoryo);
        requestScope("sumGokei", sumGokei);
        
        //20行でページ送り、2ページ目以降は30行
        int maxRow = 20;
        if(!Util.isEmpty(s.getTorihikisakiRef().getModel().getBiko())) maxRow = 20;
        List<ShukkaDto[]> pageList = new ArrayList<ShukkaDto[]>();
        int row = 0;
        List<ShukkaDto> tmpList = new ArrayList<ShukkaDto>();
        Iterator<ShukkaDto> ite = dtoList.iterator();
        while(ite.hasNext()) {
            ShukkaDto dto = ite.next();
            if(dto.getShukka().getOkurisaki().length() > 12) row++;
            for(ShukkaRec rec : dto.getShukka().getRecListRef().getModelList()) {
                
                if((Util.getStringNoNull(rec.getHinmokuRef().getModel().getName()).length() 
                        + Util.getStringNoNull(rec.getBiko()).length()) > 13) row++;
            }
            if(row + dto.getSize() > maxRow) {
                pageList.add(tmpList.toArray(new ShukkaDto[tmpList.size()]));
                tmpList.clear();
                row = 0;
                maxRow = 30;
            }
            tmpList.add(dto);
            row += dto.getSize();
        }
        
        if(tmpList.size() > 0) pageList.add(tmpList.toArray(new ShukkaDto[tmpList.size()]));

        List<Integer> empList = new ArrayList<Integer>();
        for(int i = 0; i < maxRow - row; ++i) {
            empList.add(i, i);
        }
        
        requestScope("s", s);
        requestScope("list", pageList);
        requestScope("pageCount", pageList.size());
        requestScope("empList", empList);
        requestScope("loginYago", loginYago);
        
        return forward("print.jsp");
    }
    
}
