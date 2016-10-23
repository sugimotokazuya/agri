package agri.service.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.slim3.datastore.Datastore;

import agri.meta.mail.RecMailMeta;
import agri.model.Chokubai;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.UriageCount;
import agri.model.hanbai.UriageKingaku;
import agri.model.mail.OneNotice;
import agri.model.mail.RecMail;
import agri.service.ChokubaiService;
import agri.service.HinmokuService;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.UriageCountService;
import agri.service.hanbai.UriageKingakuService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class RecMailService {

    private RecMailMeta meta = RecMailMeta.get();
    private OneNoticeService onService = new OneNoticeService();
    private ChokubaiService cService = new ChokubaiService();
    private HinmokuService hService = new HinmokuService();
    private UriageKingakuService ukService = new UriageKingakuService();
    private UriageCountService ucService = new UriageCountService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    public RecMail get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public List<RecMail> getAll() {
        return Datastore.query(meta).sort(meta.date.desc).asList();
    }
    
    public List<RecMail> getListByDateRange(Yago yago, Date start, Date end) {
        List<RecMail> list = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.greaterThanOrEqual(start), meta.date.lessThan(end))
            .sort(meta.date.desc).asList();
        return list;
    }
    
    public RecMail getByMinDate(Yago yago) {
        RecMail rm = Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()))
            .sort(meta.date.asc).limit(1).asSingle();
        return rm;
    }
    
    // TODO 初期化用
    public List<RecMail> getAllAll() {
        return Datastore.query(meta).sort(meta.date.desc).asList();
    }
    
    public void insert(RecMail recMail) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, recMail);
        tx.commit();
    }
    
    public void SuehiroOtoyo(String text) throws Exception {
        /*
        Torihikisaki t = tService.getByName("末広");
        text = text.replaceAll(",", "");
        InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
        InputStreamReader sr = new InputStreamReader(is, "UTF-8");
        StreamTokenizer st = new StreamTokenizer(sr);

        Calendar cal = Util.getCalendar();
        
        
        OneNotice on = new OneNotice();
        on.setDate(date);
        onService.insert(on);
                
        Stack<SunDto> stack = new Stack<SunDto>();
        
        int token;
        boolean isChokubaiName = true; 
        while((token = st.nextToken())!=StreamTokenizer.TT_EOF) {
            switch (token) {
            case StreamTokenizer.TT_EOL:
                break;
            case StreamTokenizer.TT_NUMBER:
                int value = (int) st.nval;
                SunDto dto = stack.lastElement();
                if(dto.isEnd()) {
                    // 店合計金額
                    dto.getUk().setKingaku(value);
                } else {
                    // 品目の数量
                    dto.getUcStack().lastElement().addCount(value);
                }
                break;
            case StreamTokenizer.TT_WORD:
                String sval = st.sval;
                if(isChokubaiName) {
                    // 店舗
                    stack.add(new SunDto(on));
                    String name = sval;
                    Chokubai chokubai = cService.getByName(t, name);
                    if(chokubai == null) {
                        chokubai = new Chokubai();
                        chokubai.getTorihikisakiRef().setModel(t);
                        chokubai.setName(name);
                        cService.insert(chokubai);
                    }
                    stack.lastElement().getUk().getChokubaiRef().setModel(chokubai);
                    isChokubaiName = false;
                } else {
                    // 品目名もしくは店合計金額
                    if("店合計金額".equals(Util.trim(st.sval))) {
                        stack.lastElement().setEnd(true);   // 店合計金額
                        isChokubaiName = true;
                    } else {
                        // 品目名
                        String name = Util.trim(st.sval);
                        Hinmoku hinmoku = hService.getByStartsWith(name);
                        if(hinmoku == null) {
                            hinmoku = new Hinmoku();
                            hinmoku.setName(name);
                            hService.insert(hinmoku);
                        }
                        UriageCount uc = new UriageCount();
                        uc.getHinmokuRef().setModel(hinmoku);
                        uc.getUriageKingakuRef().setModel(stack.lastElement().getUk());
                        stack.lastElement().ucStack.add(uc);
                    }
                }
                break;
            default:
                System.out.print("<token>" + (char)st.ttype + "</token>");
            }
        }
        */
    }
    
    public void SunshinMail(Yago yago, Date date, String text) throws Exception {
        
        Torihikisaki t = tService.getByName(yago, "サンシャイン");
        text = text.replaceAll(",", "");
        InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
        InputStreamReader sr = new InputStreamReader(is, "UTF-8");
        StreamTokenizer st = new StreamTokenizer(sr);

        OneNotice on = new OneNotice();
        on.setDate(date);
        on.getYagoRef().setModel(yago);
        onService.insert(on);
        
        Stack<SunDto> stack = new Stack<SunDto>();
        
        int token;
        boolean isChokubaiName = true; 
        while((token = st.nextToken())!=StreamTokenizer.TT_EOF) {
            switch (token) {
            case StreamTokenizer.TT_EOL:
                break;
            case StreamTokenizer.TT_NUMBER:
                int value = (int) st.nval;
                SunDto dto = stack.lastElement();
                if(dto.isEnd()) {
                    // 店合計金額
                    dto.getUk().setKingaku(value);
                } else {
                    // 品目の数量
                    dto.getUcStack().lastElement().addCount(value);
                }
                break;
            case StreamTokenizer.TT_WORD:
                String sval = st.sval;
                if(isChokubaiName) {
                    // 店舗
                    stack.add(new SunDto(on));
                    String name = sval;
                    Chokubai chokubai = cService.getByName(t, name);
                    if(chokubai == null) {
                        chokubai = new Chokubai();
                        chokubai.getTorihikisakiRef().setModel(t);
                        chokubai.setName(name);
                        cService.insert(chokubai);
                    }
                    stack.lastElement().getUk().getChokubaiRef().setModel(chokubai);
                    isChokubaiName = false;
                } else {
                    // 品目名もしくは店合計金額
                    if("店合計金額".equals(Util.trim(st.sval))) {
                        stack.lastElement().setEnd(true);   // 店合計金額
                        isChokubaiName = true;
                    } else {
                        // 品目名
                        String name = Util.trim(st.sval);
                        
                        Hinmoku hinmoku = null;
                        if(name.length() >= 8){
                            hinmoku = hService.getByStartsWith(yago, name);
                        } else {
                            hinmoku = hService.getByName(yago, name);
                        }
                        if(hinmoku == null) {
                            hinmoku = new Hinmoku();
                            hinmoku.getYagoRef().setModel(yago);
                            hinmoku.setName(name);
                            hService.insert(hinmoku);
                        }
                        UriageCount uc = new UriageCount();
                        uc.getHinmokuRef().setModel(hinmoku);
                        uc.getUriageKingakuRef().setModel(stack.lastElement().getUk());
                        stack.lastElement().ucStack.add(uc);
                    }
                }
                break;
            default:
                System.out.print("<token>" + (char)st.ttype + "</token>");
            }
        }

        // UriageKingakuとUriageCountを登録
        Iterator<SunDto> ite = stack.iterator();
        while(ite.hasNext()) {
            SunDto dto = ite.next();
            ukService.insert(dto.getUk());
            Iterator<UriageCount> ucIte = dto.getUcStack().iterator();
            while(ucIte.hasNext()) {
                UriageCount uc = ucIte.next();
                ucService.insert(uc);
            }
        }
    }

    public void update(RecMail rm) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, rm);
        tx.commit();
    }

    public void delete(Key key, Long version) {
        RecMail mail = Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, mail.getKey());
        tx.commit();
    }
    
    public class SunDto {
        public SunDto(OneNotice on) {
            uk = new UriageKingaku();
            uk.getOneNoticeRef().setModel(on);
            ucStack = new Stack<UriageCount>();
        }
        private UriageKingaku uk;
        private Stack<UriageCount> ucStack;
        private boolean end = false;
        
        public UriageKingaku getUk() {
            return uk;
        }
        public void setUk(UriageKingaku uk) {
            this.uk = uk;
        }
        public Stack<UriageCount> getUcStack() {
            return ucStack;
        }
        public void setUcStack(Stack<UriageCount> ucStack) {
            this.ucStack = ucStack;
        }
        public boolean isEnd() {
            return end;
        }
        public void setEnd(boolean end) {
            this.end = end;
        }
    }
}
