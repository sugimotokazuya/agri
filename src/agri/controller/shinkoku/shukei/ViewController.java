package agri.controller.shinkoku.shukei;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.TekiyoShukeiService;

public class ViewController extends BaseController {

    private KamokuService kamokuService = new KamokuService();
    private TekiyoShukeiService shukeiService = new TekiyoShukeiService();
    
    @Override
    public boolean validateAuth() {
        Kamoku kamoku = kamokuService.get(asKey("kamoku"));
        if(!loginYago.equals(kamoku.getYagoRef().getModel())) return false;
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        int year = asInteger("year");
        Kamoku kamoku = kamokuService.get(asKey("kamoku"));
        requestScope("kamoku", kamoku);
        requestScope("year", year);
        
        // start
        shukeiService.shukei(loginYago, year);
        requestScope("shukeiList", shukeiService.getList(year, kamoku));
        
        // end

//        HashMap<Key,ShukeiDto> karikata = new HashMap<Key, ShukeiDto>();
//        HashMap<Key,ShukeiDto> kashikata = new HashMap<Key, ShukeiDto>();
//        
//        List<Shiwake> list = service.getByDateRange(
//            start.getTime(), end.getTime());
//
//        for (Shiwake shiwake : list) {
//            Tekiyo tekiyo = shiwake.getTekiyoRef().getModel();
//            if(shiwake.getKarikataKamokuRef().getKey().equals(
//                    kamoku.getKey())) {
//                if(!karikata.containsKey(tekiyo.getKey())) {
//                    ShukeiDto dto = new ShukeiDto();
//                    dto.setTekiyo(tekiyo);
//                    karikata.put(tekiyo.getKey(), dto);
//                }
//                ShukeiDto dto = karikata.get(tekiyo.getKey());
//                dto.setKarikata(dto.getKarikata() + shiwake.getKarikataKingaku());
//            } else if(shiwake.getKashikataKamokuRef().getKey().equals(
//                kamoku.getKey())) {
//                if(!kashikata.containsKey(tekiyo.getKey())) {
//                    ShukeiDto dto = new ShukeiDto();
//                    dto.setTekiyo(tekiyo);
//                    kashikata.put(tekiyo.getKey(), dto);
//                }
//                ShukeiDto dto = kashikata.get(tekiyo.getKey());
//                dto.setKashikata(dto.getKashikata() + shiwake.getKashikataKingaku());
//            }
//        }
//
//        requestScope("karikata", karikata);
//        requestScope("kashikata", kashikata);
        
        if(request.getAttribute("print") == null) {
            if(kamoku.getKubun() == 4) {
                return forward("view4.jsp");
            } else {
                return forward("view.jsp");
            }
        } else {
            if(kamoku.getKubun() == 4) {
                return forward("view4Print.jsp");
            } else {
                return forward("viewPrint.jsp");
            }
        }
    }
//    
//    public class ShukeiDto {
//
//        private Tekiyo tekiyo;
//        private int karikata;
//        private int kashikata;
//        public Tekiyo getTekiyo() {
//            return tekiyo;
//        }
//        public void setTekiyo(Tekiyo tekiyo) {
//            this.tekiyo = tekiyo;
//        }
//        public int getKarikata() {
//            return karikata;
//        }
//        public void setKarikata(int karikata) {
//            this.karikata = karikata;
//        }
//        public int getKashikata() {
//            return kashikata;
//        }
//        public void setKashikata(int kashikata) {
//            this.kashikata = kashikata;
//        }
// 
//        
//    }
}
