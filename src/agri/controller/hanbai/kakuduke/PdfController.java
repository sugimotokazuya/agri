package agri.controller.hanbai.kakuduke;

import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Kakuduke;
import agri.model.hanbai.Shukka;
import agri.model.hanbai.ShukkaRec;
import agri.service.Util;
import agri.service.hanbai.KakudukeService;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.awt.Color;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfController extends BaseController {
   
    
    @Override
    public boolean validateAuth() {
        return loginUser.isAuthShukkaView();
    }
    
    private KakudukeService kService = new KakudukeService();
    private static String YEAR = "/hanbai/kakuduke/year";
    private static String MONTH = "/hanbai/kakuduke/month";
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        if(sessionScope(YEAR) == null) sessionScope(YEAR, year);
        
        int yearI = asInteger("year") == null ? (int) sessionScope(YEAR) : asInteger("year");
        Integer month = asInteger("month") == null ? (Integer) sessionScope(MONTH) : asInteger("month");
        if(month == null) month = cal.get(Calendar.MONTH);
        sessionScope(YEAR, yearI);
        sessionScope(MONTH, month);
        
        List<Kakuduke> list = kService.getList(yearI,month, loginYago);
        NumberFormat nfNum = NumberFormat.getNumberInstance();

        //文書オブジェクトを生成
        Document doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); 

        //出力先(アウトプットストリーム)の生成
        response.setContentType("application/pdf");
        OutputStream os = response.getOutputStream();
        
        //アウトプットストリームをPDFWriterに設定
        PdfWriter pdfwriter = PdfWriter.getInstance(doc, os);
        
        //フォントの設定
        BaseFont baseFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
        SimpleDateFormat sdf = Util.getSimpleDateFormat("yyyy年MM月dd日");
        float fontSize = 9;
        
        // ヘッダー
        HeaderFooter header = new HeaderFooter(new Phrase(
            new Chunk("別添様式第６号　（別記様式第１２号１　有機農産物の調査申請書関係）",new Font(baseFont, 10))),false);
        header.setAlignment(Element.ALIGN_LEFT);
        header.setBorderColor(Color.white);
        doc.setHeader(header);
        
        //　フッター
        /*
        HeaderFooter footer = new HeaderFooter(new Phrase(
            new Chunk("",new Font(baseFont, 10))),false);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setBorderColor(Color.white);
        doc.setFooter(footer);
        */
        
        // ページ番号をフッターに
        pdfwriter.setPageEvent(new PdfPageEventHelper() {
            // 次のページに切り替わる直前に呼び出される。
            public void onEndPage(PdfWriter writer, Document document)
            {
                try {
                    final PdfContentByte cb = writer.getDirectContent();
                    final int BETWEEN_TEXT_AND_PAGE = 20;
                    BaseFont baseFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
                    cb.saveState();
    
                    cb.beginText();
                    cb.setFontAndSize(baseFont, 10);
    
                    // ページ番号の表示内容
                    final String pageText = writer.getPageNumber() + " ";
                    // 右詰の時、少しずれるので末尾に空白を１つ追加しておく。
    
                    // ページ番号のY座標
                    final float y = document.bottom(-BETWEEN_TEXT_AND_PAGE);
    
                    cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, pageText, document.right(), y, 0);
    
                    cb.endText();
                    
                    cb.restoreState();
                    
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        //文章オブジェクト オープン
        doc.open();
        int paddingSize = 5;
        
        //タイトル
        Paragraph p = new Paragraph("格付管理", new Font(baseFont, 15));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {50,50});
        table.setSpacingBefore(10);

        String dateWide = "";
        if(list.size() > 0) {
            dateWide = "期間：" + yearI + "年" + (month + 1) + "月";
        }
        PdfPCell cell = new PdfPCell(new Paragraph(dateWide, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        Calendar now = Util.getCalendar();
        cell = new PdfPCell(new Paragraph("作成　" + sdf.format(now.getTime()), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        doc.add(table);
        
        table = new PdfPTable(12);
        //table.setHeaderRows(2); なぜかrowspanがうまくいかない
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {11,13,7,5,5,5,8,21,6,6,6,7});
        table.setSpacingBefore(10);
        
        // 1
        cell = new PdfPCell(new Paragraph("出荷日", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 2
        cell = new PdfPCell(new Paragraph("品目", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 3
        cell = new PdfPCell(new Paragraph("出荷形態", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 4
        cell = new PdfPCell(new Paragraph("数量", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 5
        cell = new PdfPCell(new Paragraph("重量", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 6
        cell = new PdfPCell(new Paragraph("検査結果", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 7
        cell = new PdfPCell(new Paragraph("不合格品処分方法", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 8
        cell = new PdfPCell(new Paragraph("出荷先", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 9-11
        cell = new PdfPCell(new Paragraph("JAS格付表示", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setColspan(3);
        table.addCell(cell);
        
        // 12
        cell = new PdfPCell(new Paragraph("備考（担当者）", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        // 9
        cell = new PdfPCell(new Paragraph("受入", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 10
        cell = new PdfPCell(new Paragraph("使用数", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        // 11
        cell = new PdfPCell(new Paragraph("残数", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        int sum = 0;
        
        for(Kakuduke k : list) {
         
            // 年月日
            cell = new PdfPCell(new Paragraph(sdf.format(k.getDate()), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 品目
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            if(k.getShukkaRef().getModel() != null) {
                for(ShukkaRec rec : k.getShukkaRef().getModel().getRecListRef().getModelList()) {
                    if(!first) sb.append(",");
                    first = false;
                    sb.append(rec.getHinmokuRef().getModel().getName());
                }
            }
            if(k.getShukkaRecRef().getModel() != null) {
                sb.append(k.getShukkaRecRef().getModel().getHinmokuRef().getModel().getName());
            }
            cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 出荷形態
            sb = new StringBuilder();
            first = true;
            if(k.getShukkaRef().getModel() != null) {
                for(ShukkaRec rec : k.getShukkaRef().getModel().getRecListRef().getModelList()) {
                    if(!first) sb.append(",");
                    first = false;
                    sb.append(rec.getShukkaKeitaiRef().getModel().getHoso());
                }
            }
            if(k.getShukkaRecRef().getModel() != null) {
                sb.append(k.getShukkaRecRef().getModel().getShukkaKeitaiRef().getModel().getHoso());
            }
            cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 数量
            sb = new StringBuilder();
            first = true;
            if(k.getShukkaRef().getModel() != null) {
                for(ShukkaRec rec : k.getShukkaRef().getModel().getRecListRef().getModelList()) {
                    if(!first) sb.append(",");
                    first = false;
                    sb.append(rec.getSuryo());
                }
            }
            if(k.getShukkaRecRef().getModel() != null) {
                sb.append(k.getShukkaRecRef().getModel().getSuryo());
            }
            cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 重量
            sb = new StringBuilder();
            first = true;
            if(k.getShukkaRef().getModel() != null) {
                for(ShukkaRec rec : k.getShukkaRef().getModel().getRecListRef().getModelList()) {
                    if(!first) sb.append(",");
                    first = false;
                    sb.append(rec.getShukkaKeitaiRef().getModel().getJuryo());
                }
            }
            if(k.getShukkaRecRef().getModel() != null) {
                sb.append(k.getShukkaRecRef().getModel().getShukkaKeitaiRef().getModel().getJuryo());
            }
            cell = new PdfPCell(new Paragraph(sb.toString(), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 検査結果
            cell = new PdfPCell(new Paragraph("合格", new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 不合格品
            cell = new PdfPCell(new Paragraph("", new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 出荷先
            Shukka shukka = k.getShukkaRef().getModel();
            if(shukka == null && k.getShukkaRecRef().getModel() != null) {
                shukka = k.getShukkaRecRef().getModel().getShukkaRef().getModel();
            }
            String str = "";
            if(shukka != null) {
                if(Util.isEmpty(shukka.getOkurisaki())) {
                    str = shukka.getTorihikisakiRef().getModel().getName();
                } else {
                    str = shukka.getOkurisaki();
                }
            }
            cell = new PdfPCell(new Paragraph(str, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 受け入れ
            str = "";
            if(k.getPlus() > 0) {
                str = nfNum.format(k.getPlus());
            }
            cell = new PdfPCell(new Paragraph(str, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 使用数
            str = "";
            if(k.getPlus() < 0) {
                str = nfNum.format(k.getPlus() * -1);
            }
            cell = new PdfPCell(new Paragraph(str, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 残数
            sum += k.getPlus();
            cell = new PdfPCell(new Paragraph(nfNum.format(sum), new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
            
            // 備考
            str = "";
            if(Util.isEmpty(k.getBiko())) {
                str = k.getUserRef().getModel().getName();
            } else {
                str = k.getBiko();
            }
            cell = new PdfPCell(new Paragraph(str, new Font(baseFont, fontSize)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            table.addCell(cell);
        }
        
        doc.add(table);
        
        //文章オブジェクト クローズ
        doc.close();
            
        //PDFWriter クローズ
        pdfwriter.close();
        
        return null;
    }
}
