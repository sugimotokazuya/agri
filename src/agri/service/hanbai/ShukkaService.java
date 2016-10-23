package agri.service.hanbai;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import agri.meta.hanbai.ShukkaMeta;
import agri.meta.hanbai.ShukkaRecMeta;
import agri.model.Hinmoku;
import agri.model.Torihikisaki;
import agri.model.Yago;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaRec;
import agri.service.Const;
import agri.service.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class ShukkaService {
    
    private ShukkaMeta meta = ShukkaMeta.get();
    private ShukkaRecMeta recMeta = ShukkaRecMeta.get();
    private ShukkaRecService recService = new ShukkaRecService();
    private KakudukeService kService = new KakudukeService();
    
 // TODO 初期化用
    public List<Shukka> getAllAll() {
        return Datastore.query(meta).asList();
    }
    
    public List<Shukka> getList(Yago yago, Date start, Date end) {
        return Datastore.query(meta).filter(
            meta.date.greaterThanOrEqual(start)).filter(meta.date.lessThan(end)).asList();
    }
    
    public void delete(Key key, long version) {
        Shukka s = Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
        deleteRec(s.getKey());
        Transaction tx = Datastore.beginTransaction();
        Datastore.delete(tx, s.getKey());
        tx.commit();
    }

    public void insert(Shukka s, List<ShukkaRec> recList) {
        s.setTorihikisakiName(s.getTorihikisakiRef().getModel().getName());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
        Iterator<ShukkaRec> ite =  recList.iterator();
        while(ite.hasNext()) {
            tx = Datastore.beginTransaction();
            ShukkaRec rec = ite.next();
            rec.setHinmokuName(rec.getHinmokuRef().getModel().getName());
            rec.setHosoName(rec.getShukkaKeitaiRef().getModel().getHoso());
            rec.setDate(s.getDate());
            Datastore.put(tx, rec);
            tx.commit();
        }
    }
    
    public void update(Shukka s, List<ShukkaRec> recList) {
        deleteRec(s.getKey());
        insert(s, recList);
    }
    
    public void update(Shukka s) {
        s.setTorihikisakiName(s.getTorihikisakiRef().getModel().getName());
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
    }
    
    public void clearTorihikisaki(Shukka s) {
        s.getTorihikisakiRef().clear();
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, s);
        tx.commit();
    }
    
    private void deleteRec(Key parentKey) {
        List<ShukkaRec> delList = Datastore.query(recMeta).filter(
            recMeta.shukkaRef.equal(parentKey)).asList();
        Kakuduke k = kService.getByShukka(parentKey);
        if(k != null) kService.delete(k);
        for(ShukkaRec rec : delList) {
            k = kService.getByShukkaRec(rec);
            if(k != null) kService.delete(k);
            Transaction tx = Datastore.beginTransaction();
            Datastore.delete(tx, rec.getKey());
            tx.commit();
        }
    }
    
    public Shukka get(Key key, Long version) {
        return Datastore.query(meta).filter(
            meta.key.equal(key), meta.version.equal(version)).asSingle();
    }
    
    public Shukka get(Key key) {
        return Datastore.query(meta).filter(
            meta.key.equal(key)).asSingle();
    }
    
    public List<ShukkaDto> getDtoList(Key[] keys) {
        
        List<ShukkaDto> list = new ArrayList<ShukkaDto>();
        
        for(int i = 0; i < keys.length; ++i) {
            ShukkaDto dto = new ShukkaDto();
            dto.setShukka(get(keys[i]));
            dto.setRecList(recService.getList(dto.getShukka()));
            list.add(dto);
        }
        
        Collections.sort(list, new DtoComparator());
        return list;
    }
    
    public int getSumGokei(List<ShukkaDto> list) {
        int seikyuKingaku = 0;
        Iterator<ShukkaDto> ite = list.iterator();
        while(ite.hasNext()) {
            ShukkaDto dto = ite.next();
            seikyuKingaku += dto.getGokei();
        }
        return seikyuKingaku;
    }
    
    public List<Shukka> getByTorihikisaki(Torihikisaki t) {
        return Datastore.query(meta).filter(meta.torihikisakiRef.equal(t.getKey())).asList();
    }
    
    public List<Shukka> getChokubai(Yago yago, Date start, Date end) {
        return Datastore.query(meta).filter(
            meta.yagoRef.equal(yago.getKey()),
            meta.date.greaterThanOrEqual(start),
            meta.date.lessThan(end), meta.kaishu.equal(0)).asList();
    }
    
    public Map<Hinmoku, ShukeiDto> getShukeiMap(Yago yago, Integer year, Key tKey) {
        
        ModelQuery<Shukka> mq = Datastore.query(meta);
        mq = mq.filter(meta.yagoRef.equal(yago.getKey()));
        if(year != null) {
            Calendar start = Util.getCalendar();
            start.clear();
            start.set(Calendar.YEAR, year);
            start.set(Calendar.MONTH, 0);
            start.set(Calendar.DAY_OF_MONTH, 1);
            Calendar end = Util.getCalendar();
            end.setTimeInMillis(start.getTimeInMillis());
            end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
            mq = mq.filter(meta.date.greaterThanOrEqual(start.getTime()),
            meta.date.lessThan(end.getTime()));
        }
        
        if(tKey != null) {
            mq = mq.filter(meta.torihikisakiRef.equal(tKey));
        }
        
        List<Shukka> sList =  mq.sort(meta.date.desc).asList();
        Iterator<Shukka> ite = sList.iterator();
        Map<Hinmoku, ShukeiDto> map = new HashMap<Hinmoku, ShukeiDto>();
        while(ite.hasNext()) {
            Shukka s = ite.next();
            List<ShukkaRec> recList = recService.getList(s);
            Iterator<ShukkaRec> recIte = recList.iterator();
            while(recIte.hasNext()) {
                ShukkaRec rec = recIte.next();
                Hinmoku h = rec.getHinmokuRef().getModel();
                ShukeiDto dto;
                if(map.containsKey(h)) {
                    dto = map.get(h);
                } else {
                    dto = new ShukeiDto();
                    dto.setHinmoku(h);
                    map.put(h, dto);
                }
                int shokei = (int)(rec.getTanka() * rec.getSuryo());
                if(s.getTax() == 0) {
                    float tax = shokei - (shokei / (1 + ((float)Const.TAX / 100)));
                    dto.setKingaku(dto.getKingaku() + shokei - (int)tax);
                    dto.setTax(dto.getTax() + (int)tax);
                    
                } else {
                    dto.setKingaku(dto.getKingaku() + shokei);
                    float tax = shokei * ((float)Const.TAX / 100);
                    dto.setTax(dto.getTax() + (int) tax);
                }
                
            }
        }
        
        return map;
    }
    
    public List<ShukkaDto> getDtoList(Yago yago, int year, int month, Key tKey) {
        
        List<ShukkaDto> list = new ArrayList<ShukkaDto>();
        
        ModelQuery<Shukka> mq = Datastore.query(meta);
        mq = mq.filter(meta.yagoRef.equal(yago.getKey()));
        Calendar start = Util.getCalendar();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month == 12 ? 0 : month);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTimeInMillis(start.getTimeInMillis());
        if(month == 12) {
            end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
        } else {
            end.set(Calendar.MONTH, start.get(Calendar.MONTH) + 1);
        }
        mq = mq.filter(meta.date.greaterThanOrEqual(start.getTime()),
        meta.date.lessThan(end.getTime()));
        
        if(tKey != null) {
            mq = mq.filter(meta.torihikisakiRef.equal(tKey));
        }
        
        List<Shukka> sList =  mq.sort(meta.date.desc).asList();
        Iterator<Shukka> ite = sList.iterator();
        while(ite.hasNext()) {
            ShukkaDto dto = new ShukkaDto();
            dto.setShukka(ite.next());
            dto.setRecList(recService.getList(dto.getShukka()));
            list.add(dto);
        }
        
        return list;
    }
    
    public class ShukeiDto {
        
        private Hinmoku hinmoku;
        private int kingaku;
        private int tax;
        public int getKingaku() {
            return kingaku;
        }
        public void setKingaku(int kingaku) {
            this.kingaku = kingaku;
        }
        public int getTax() {
            return tax;
        }
        public void setTax(int tax) {
            this.tax = tax;
        }
        public Hinmoku getHinmoku() {
            return hinmoku;
        }
        public void setHinmoku(Hinmoku hinmoku) {
            this.hinmoku = hinmoku;
        }
        
    }
    
    public class ShukkaDto {
        private Shukka shukka;
        private List<ShukkaRec> recList;
        
        public int getSize() {
            return recList.size();
        }
        
        public int getShokei() {
            Iterator<ShukkaRec> ite = recList.iterator();
            int sum = 0;
            while(ite.hasNext()) {
                ShukkaRec rec = ite.next();
                sum += rec.getTanka() * rec.getSuryo();
            }
            return sum;
        }
        
        public int getGokei() {
            return getShokei() + shukka.getTax() + shukka.getSoryo();
        }
        
        public Shukka getShukka() {
            return shukka;
        }
        public void setShukka(Shukka shukka) {
            this.shukka = shukka;
        }
        public List<ShukkaRec> getRecList() {
            return recList;
        }
        public void setRecList(List<ShukkaRec> recList) {
            this.recList = recList;
        }
    }
    
    public void printNohin(Shukka s, Document doc) throws Exception {
        
        Yago loginYago = s.getYagoRef().getModel();
        
        //フォントの設定
        BaseFont baseFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
        SimpleDateFormat sdf = Util.getSimpleDateFormat("yyyy年MM月dd日");
        NumberFormat nfNum = NumberFormat.getNumberInstance();
        float fontSize = 9;
        
        List<ShukkaRec> recList = recService.getList(s);
        Torihikisaki t = s.getTorihikisakiRef().getModel();
        
        int paddingSize = 5;

        // 金額の計算
        int kingaku = 0;
        for(ShukkaRec rec : recList) {
            kingaku += rec.getTanka() * rec.getSuryo();
        }
        int seikyuKingaku = kingaku + s.getSoryo()+s.getTax();
        
        //タイトル
        String title = "納品書";
        if(t.getNohin() == 3) {
            title = title + " 兼 請求書";
        }
        Paragraph p = new Paragraph(title, new Font(baseFont, 24));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {67,33});
        table.setSpacingBefore(20);

        String atena = "";
        String okurisaki = "";
        switch(t.getNohin()) {
        case 1:
        case 2:
        case 3:
            atena = Util.isEmpty(s.getOkurisaki()) ? t.getName() : s.getOkurisaki();
            break;
        case 4:
        case 5:
            atena = t.getName();
            if(!Util.isEmpty(s.getOkurisaki())) okurisaki = s.getOkurisaki();
            break;
        }
        Paragraph para = new Paragraph(atena, new Font(baseFont, 14));
        if(!Util.isEmpty(okurisaki)) {
            para.add(Const.KAIGYO);
            para.add("（送り先：");
            para.add(okurisaki);
            para.add(" 様）");
        }
        para.add(" 様");
        para.add(Const.KAIGYO);
        para.add(Const.KAIGYO);
        para.add("ご注文ありがとうございました。");
        if(!Util.isEmpty(t.getBiko())) {
            Phrase phrase = new Phrase(Const.KAIGYO, new Font(baseFont, 12));
            phrase.add(Const.KAIGYO);
            phrase.add(t.getBiko());
            para.add(phrase);
        }
        PdfPCell cell = new PdfPCell(para);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        StringBuilder sb = new StringBuilder(loginYago.getName());
        sb.append(Const.KAIGYO);
        sb.append(loginYago.getToiawase());
        cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, 10)));
        cell.setLeading(2f, 1f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        if(t.getNohin() == 3) {
            cell = new PdfPCell(new Paragraph("下記の通りご請求申し上げます。", new Font(baseFont, 12)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("　", new Font(baseFont, 10)));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("ご請求額：" + nfNum.format(seikyuKingaku) + "円", new Font(baseFont, 14)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            Calendar cal = Util.getCalendar();
            Calendar limitCal = Util.getCalendar();
            limitCal.add(Calendar.MONTH, 2);
            limitCal.set(Calendar.DAY_OF_MONTH, 1);
            limitCal.add(Calendar.DAY_OF_MONTH, -1);

            sb = new StringBuilder();
            sb.append("発行日：");
            sb.append(sdf.format(cal.getTime()));
            sb.append(Const.KAIGYO);
            sb.append("支払期限：");
            sb.append(sdf.format(limitCal.getTime()));
            cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, 10)));
            cell.setLeading(3f, 1f);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            doc.add(table);
            
            para = new Paragraph("下記いずれかの銀行口座までお振り込みくださいますようお願い申し上げます。"
                , new Font(baseFont, 10));
            doc.add(para);
            
            table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setWidths(new int[] {34,33,33});
            table.setSpacingBefore(5);
            
            cell = new PdfPCell(new Paragraph(loginYago.getFurikomisaki1(), new Font(baseFont, 10)));
            cell.setLeading(3f, 1f);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(loginYago.getFurikomisaki2(), new Font(baseFont, 10)));
            cell.setLeading(3f, 1f);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(loginYago.getFurikomisaki3(), new Font(baseFont, 10)));
            cell.setLeading(3f, 1f);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
        
        doc.add(table);
        
        String fuseStr = "*****";
        String tanka = fuseStr;
        String kingakuStr = fuseStr;
        String tax = fuseStr;
        String shokei = fuseStr;
        String gokei = fuseStr;
        String soryo = fuseStr;
        switch(t.getNohin()) {
        case 2:
        case 3:
        case 5:
            kingakuStr = nfNum.format(kingaku);
            tax = nfNum.format(s.getTax());
            shokei = nfNum.format(seikyuKingaku + s.getTax());
            soryo = nfNum.format(s.getSoryo());
            gokei = nfNum.format(seikyuKingaku);
            break;
        }

        table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {20,16,16,16,16,16});
        table.setSpacingBefore(20);
        
        // 1
        cell = new PdfPCell(new Paragraph("出荷日", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 2
        cell = new PdfPCell(new Paragraph("金額(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 3
        cell = new PdfPCell(new Paragraph("消費税(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);

        // 4
        cell = new PdfPCell(new Paragraph("小計(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 5
        cell = new PdfPCell(new Paragraph("送料(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 6
        cell = new PdfPCell(new Paragraph("合計(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 1
        cell = new PdfPCell(new Paragraph(sdf.format(s.getDate()), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 2
        cell = new PdfPCell(new Paragraph(kingakuStr, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 3
        cell = new PdfPCell(new Paragraph(tax, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 4
        cell = new PdfPCell(new Paragraph(shokei, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 5
        cell = new PdfPCell(new Paragraph(soryo, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 6
        cell = new PdfPCell(new Paragraph(gokei, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        doc.add(table);
        
        table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {20,43,10,7,10,10});
        table.setSpacingBefore(20);
        
        // 1
        cell = new PdfPCell(new Paragraph("商品名", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 2
        cell = new PdfPCell(new Paragraph("備考", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 3
        cell = new PdfPCell(new Paragraph("単価(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 4
        cell = new PdfPCell(new Paragraph("数量", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 5
        cell = new PdfPCell(new Paragraph("形態", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 6
        cell = new PdfPCell(new Paragraph("金額(円)", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        for(ShukkaRec rec : recList) {
            
            switch(t.getNohin()) {
            case 2:
            case 3:
            case 5:
                tanka = nfNum.format(rec.getTanka());
                kingakuStr = nfNum.format(rec.getTanka() * rec.getSuryo());
            }
            
            // 商品名
            cell = new PdfPCell(new Paragraph(rec.getHinmokuName(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 備考
            cell = new PdfPCell(new Paragraph(rec.getBiko(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 単価
            cell = new PdfPCell(new Paragraph(tanka, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 数量
            cell = new PdfPCell(new Paragraph(nfNum.format(rec.getSuryo()), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 形態
            cell = new PdfPCell(new Paragraph(
                rec.getShukkaKeitaiRef().getModel().getHoso()
                , new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 金額
            cell = new PdfPCell(new Paragraph(kingakuStr, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
        }
        
        doc.add(table);
    }
    

    public class DtoComparator implements java.util.Comparator<ShukkaDto> {
        @Override
        public int compare(ShukkaDto s, ShukkaDto t) {

            return s.getShukka().getDate().compareTo(t.getShukka().getDate());
        }
    }
}
