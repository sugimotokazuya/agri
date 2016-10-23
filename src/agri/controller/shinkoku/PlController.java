package agri.controller.shinkoku;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.shinkoku.Kamoku;
import agri.model.shinkoku.TekiyoShukei;
import agri.service.Util;
import agri.service.shinkoku.KamokuService;
import agri.service.shinkoku.TekiyoShukeiService;

public class PlController extends BaseController {

    private TekiyoShukeiService service = new TekiyoShukeiService();
    private KamokuService kamokuService = new KamokuService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShinkoku();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - service.getMinYear(loginYago) + 1;
        List<Integer> koubunList = new ArrayList<Integer>();
        
        requestScope("fusaiList", createPlDtos(2, year, yearCount, koubunList));
        requestScope("shunyuList", createPlDtos(3, year, yearCount, koubunList));
        requestScope("shishutsuList", createPlDtos(4, year, yearCount, koubunList));
        requestScope("shisanList", createPlDtos(1, year, yearCount, koubunList));
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        return forward("pl.jsp");
    }
    
    private List<PlDto> createPlDtos(int kubun, int year, int yearCount, List<Integer> koubunList) {
        List<Kamoku> kamokuList = kamokuService.getByKubun(loginYago, kubun);
        
        List<PlDto> dtos = new ArrayList<PlDto>();
        for(Kamoku kamoku : kamokuList) {
            PlDto dto = new PlDto();
            dto.setKamoku(kamoku);
            List<Integer> values = new ArrayList<Integer>();
            for(int i = 0; i < yearCount; ++i) {
                if(kubun==4) koubunList.add(new Integer(0));
                List<TekiyoShukei> shukeiList = 
                    service.getList(year - i, kamoku);
                int result = 0;
                for(TekiyoShukei shukei : shukeiList) {
                    result += keisan(shukei, kubun,koubunList, i);
                }
                values.add(result);
            }
            dto.setValues(values);
            dtos.add(dto);
        }
        if(kubun==1) {
            Kamoku koubunKamoku = new Kamoku();
            koubunKamoku.setName("事業主貸（控分）");
            PlDto koubunDto = new PlDto();
            koubunDto.setKamoku(koubunKamoku);
            koubunDto.setValues(koubunList);
            dtos.add(koubunDto);
            // 空で追加されるバグの原因が分からないので、強制削除している
            if(koubunList.size() > yearCount) {
                for(int i = koubunList.size() - 1; i + 1 > yearCount; --i) {
                    koubunList.remove(i);
                }
            }
        }
        
        Kamoku kamoku = new Kamoku();
        kamoku.setName("合計");
        PlDto sumDto = new PlDto();
        sumDto.setKamoku(kamoku);
        List<Integer> values = new ArrayList<Integer>();
        for(int i = 0; i < yearCount; ++i) {
            int result = 0;
            for(PlDto dto : dtos) {
                result += dto.getValues().get(i);
            }
            values.add(result);
        }
        sumDto.setValues(values);
        dtos.add(sumDto);
        return dtos;
    }
    
    private int keisan(TekiyoShukei shukei, int kubun, List<Integer> koubunList, int i) {
        int result = 0;
        switch(kubun) {
        case 1:
            result = shukei.getKarikata() - shukei.getKashikata();
            break;
        case 2: case 3:
            result = shukei.getKashikata() - shukei.getKarikata();
            break;
        case 4:
            result = shukei.getSannyu() - shukei.getKashikata();
            koubunList.set(i, koubunList.get(i) + (shukei.getKarikata() - shukei.getSannyu())); 
            break;
        }
        return result;
    }
    
    public class PlDto {
        private Kamoku kamoku;
        private List<Integer> values;
        public Kamoku getKamoku() {
            return kamoku;
        }
        public void setKamoku(Kamoku kamoku) {
            this.kamoku = kamoku;
        }
        public List<Integer> getValues() {
            return values;
        }
        public void setValues(List<Integer> values) {
            this.values = values;
        }
        
    }
}
