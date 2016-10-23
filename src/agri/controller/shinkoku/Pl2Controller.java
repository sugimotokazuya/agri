package agri.controller.shinkoku;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.Shiwake;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.ShiwakeService;

public class Pl2Controller extends BaseController {

    private KamokuService kamokuService = new KamokuService();
    private ShiwakeService shiwakeService = new ShiwakeService(); 
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = asInteger("year") == null ? cal.get(Calendar.YEAR) : asInteger("year");
        int month = asInteger("month") == null ? cal.get(Calendar.MONTH) : asInteger("month");
        
        requestScope("fusaiList", createPlDtos(2, year, month));
        requestScope("shunyuList", createPlDtos(3, year, month));
        requestScope("shishutsuList", createPlDtos(4, year, month));
        requestScope("shisanList", createPlDtos(1, year, month));
        return forward("pl2.jsp");
    }
    
    private List<PlDto> createPlDtos(int kubun, int year, int month) {
        
        List<Kamoku> kamokuList = kamokuService.getByKubun(loginYago, kubun);
        Date[] dateRange = Util.createOneMonthRange(year, month);
        
        List<PlDto> dtos = new ArrayList<PlDto>();
        for(Kamoku kamoku : kamokuList) {
            PlDto dto = new PlDto();
            dto.setKamoku(kamoku);
            int value = 0;
            List<Shiwake> list = shiwakeService.getByKariKamokuDateRange(kamoku, dateRange[0], dateRange[1]);
            for(Shiwake shiwake : list) {
                value += shiwake.getKarikataKingaku();
            }
            
            list = shiwakeService.getByKashiKamokuDateRange(kamoku, dateRange[0], dateRange[1]);
            for(Shiwake shiwake : list) {
                value -= shiwake.getKashikataKingaku();
            }
            
            dto.setValue(value);
            dtos.add(dto);
        }
            return dtos;
    }
    
    
    public class PlDto {
        private Kamoku kamoku;
        private int value;
        public Kamoku getKamoku() {
            return kamoku;
        }
        public void setKamoku(Kamoku kamoku) {
            this.kamoku = kamoku;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
       
        
    }
}
