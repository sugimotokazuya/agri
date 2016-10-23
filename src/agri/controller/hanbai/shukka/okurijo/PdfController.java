package agri.controller.hanbai.shukka.okurijo;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import agri.controller.BaseController;
import agri.model.Torihikisaki;
import agri.model.hanbai.Okurisaki;
import agri.service.TorihikisakiService;
import agri.service.Util;
import agri.service.hanbai.OkurisakiService;

import com.google.appengine.api.datastore.Key;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class PdfController extends BaseController {

    private OkurisakiService oService = new OkurisakiService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    protected Navigation run() throws Exception {
        
        if(!validate()) return null;
        
        List<Okurisaki> list = new ArrayList<Okurisaki>();
        
        for(int i = 1; i < 16; ++i) {
            Key oKey = Util.isEmpty(asString("okurisaki" + i)) ? null : asKey("okurisaki" + i);
            Okurisaki o;
            if(oKey == null) {
                Key tKey = Util.isEmpty(asString("torihikisaki" + i)) ? null : asKey("torihikisaki" + i);
                if(tKey == null) continue;
                Torihikisaki t = tService.get(tKey);
                o = new Okurisaki();
                BeanUtil.copy(t, o);
            } else {
                o = oService.get(oKey);
            }
            o.setHaitatsubi(asInteger("haitatsubi" + i));
            o.setKiboujikan(asInteger("kiboujikan" + i));
            for(int j = 1; j <= asInteger("kuchisu" + i); ++j) {
                list.add(o);
            }
            
        }
        if(list.size() == 0) return null;
        
        Calendar today = Util.getCalendar();
        Util.resetTime(today);
        today.set(Calendar.YEAR, asInteger("year"));
        today.set(Calendar.MONTH, asInteger("month") - 1);
        today.set(Calendar.DAY_OF_MONTH, asInteger("day"));
        
        //文書オブジェクトを生成
        Rectangle r = new Rectangle(595, 357);
        Document doc = new Document(r, 1, 1, 1, 1); 

        //出力先(アウトプットストリーム)の生成
        response.setContentType("application/pdf");
        OutputStream os = response.getOutputStream();
        
        //アウトプットストリームをPDFWriterに設定
        PdfWriter pdfwriter = PdfWriter.getInstance(doc, os);
        
        //文章オブジェクト オープン
        doc.open();
        boolean first = true;
        
        for(Okurisaki o : list) {
            
            if(!first) doc.newPage();
            first = false;
            
          //フォントの設定
            BaseFont baseFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
            
            int baseX = 0;
            int baseY = 0;
            int x = 595;
            int y = 357;
            PdfContentByte cb = pdfwriter.getDirectContent();
            cb.saveState();
            cb.beginText();
            cb.setFontAndSize(baseFont, 5);
            
            boolean debug = asBoolean("debug") == null ? false : asBoolean("debug");
            if(debug) {
                for(x=595;x > 0; x=x-10) {
                    cb.setTextMatrix(x, 340);
                    cb.showText(String.valueOf(x));
                }
                for(y=355;y > 0; y=y-10) {
                    cb.setTextMatrix(100, y);
                    cb.showText(String.valueOf(y));
                }
            }
            
            // 郵便番号
            cb.setFontAndSize(baseFont, 14);
            String yubin = o.getYubin1() + o.getYubin2();
            for(int i = 0; i < 7; ++i) {
                cb.setTextMatrix(baseX + 125 + i * 26, baseY + 310);
                cb.showText(yubin.substring(i, i+1));
            }
            
            // 住所
            cb.setFontAndSize(baseFont, 12);
            cb.setTextMatrix(baseX + 100, baseY + 275);
            cb.showText(o.getAddress1());
            cb.setTextMatrix(baseX + 100, baseY + 247);
            cb.showText(o.getAddress2());
            cb.setTextMatrix(baseX + 100, baseY + 219);
            cb.showText(o.getAddress3());
            
            // 名前
            cb.setTextMatrix(baseX + 100, baseY + 190);
            if(o.getName().length() > 17) {
                cb.setFontAndSize(baseFont, 10);
            }
            cb.showText(o.getName());
            
            // 電話番号
            cb.setFontAndSize(baseFont, 12);
            cb.setTextMatrix(baseX + 130, baseY + 167);
            cb.showText(o.getTel());
            
            // 依頼主郵便番号
            yubin = o.getIraiYubin1() + o.getIraiYubin2();
            for(int i = 0; i < 7; ++i) {
                cb.setTextMatrix(baseX + 126 + i * 19, baseY + 140);
                cb.showText(yubin.substring(i, i+1));
            }
            
            //依頼主住所
            cb.setFontAndSize(baseFont, 10);
            cb.setTextMatrix(baseX + 100, baseY + 110);
            cb.showText(o.getIraiAddress1());
            cb.setTextMatrix(baseX + 100, baseY + 85);
            cb.showText(o.getIraiAddress2());
            
            // 依頼主
            cb.setTextMatrix(baseX + 100, baseY + 57);
            cb.showText(o.getIraiName());
            
            // 依頼主電話番号
            cb.setTextMatrix(baseX + 125, baseY + 28);
            cb.showText(o.getIraiTel());
            
            // お届け通知
            cb.circle(baseX + 372, baseY + 305, 10);
            cb.stroke();
                
            // 配達希望日
            Calendar haitatsubi = Util.getCalendar();
            haitatsubi.setTime(today.getTime());
            haitatsubi.add(Calendar.DAY_OF_MONTH, o.getHaitatsubi());
            cb.setFontAndSize(baseFont, 16);
            cb.setTextMatrix(baseX + 350, baseY + 270);
            cb.showText(String.valueOf(haitatsubi.get(Calendar.MONTH) + 1));
            cb.setTextMatrix(baseX + 390, baseY + 270);
            cb.showText(String.valueOf(haitatsubi.get(Calendar.DAY_OF_MONTH)));
            
            // 配達希望時間
            int kiboujikanX = 35 * o.getKiboujikan();
            cb.circle(baseX + 355 + kiboujikanX, baseY + 235, 10);
            cb.stroke();
            
            // 品名
            cb.setTextMatrix(baseX + 360, baseY + 190);
            cb.showText("野菜");
            
            // こわれもの
            cb.circle(baseX + 360, baseY + 145, 9);
            cb.stroke();
            cb.circle(baseX + 410, baseY + 145, 9);
            cb.stroke();
            cb.circle(baseX + 505, baseY + 145, 9);
            cb.stroke();
            cb.circle(baseX + 555, baseY + 145, 9);
            cb.stroke();
            
            cb.endText();
            cb.restoreState();
            
        }
        
        //文章オブジェクト クローズ
        doc.close();
            
        //PDFWriter クローズ
        pdfwriter.close();
        
        return null;
        
    }

    protected boolean validate() {
        Validators v = new Validators(request);
        Calendar cal = Util.getCalendar();
        v.add("year", v.required(), 
            v.longRange(cal.get(Calendar.YEAR), cal.get(Calendar.YEAR), "今年分しか入力できません。"));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        for(int i = 1; i < 16; ++i) {
            v.add("torihikisaki" + i, v.maxlength(100));
            v.add("okurisaki" + i, v.maxlength(100));
            v.add("haitatsubi" + i, v.doubleRange(1, 2));
            v.add("kiboujikan" + i, v.doubleRange(0, 6));
            v.add("kuchisu" + i, v.doubleRange(1, 9));
        }
        return v.validate();
    }
}
