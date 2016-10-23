package agri.controller.sagyo;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Navigation;
import org.slim3.util.TimeZoneLocator;

import agri.controller.BaseController;
import agri.model.Hatake;
import agri.model.User;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.SagyoItem;
import agri.model.sagyo.Sakuduke;
import agri.model.sagyo.ShiyoShizai;
import agri.model.sagyo.Shizai;
import agri.service.Util;
import agri.service.sagyo.SagyoItemService;
import agri.service.sagyo.SagyoService;

import com.google.appengine.api.datastore.Key;
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

public class SagyoItemPdfController extends BaseController {

    private SagyoItemService siService = new SagyoItemService();
    private SagyoService sService = new SagyoService();
    
    @Override
    public boolean validateAuth() {
        Key userKey = asKey("userRef");
        if(userKey != null) {
            User user = userService.get(userKey);
            if(!user.getYagoRef().getModel().equals(loginYago)) return false;
        }
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int yearCount = year - 2013 + 1;
        int[] years = new int[yearCount];
        for(int i = 0; i < yearCount; ++i) {
            years[i] = year - i;
        }
        requestScope("years", years);
        
        Integer yearI = asInteger("year");
        cal = Util.getCalendar();
        if(yearI == null) yearI = cal.get(Calendar.YEAR);
        
        requestScope("year", yearI);
        requestScope("siList", siService.getAll(loginYago));
        
        if(Util.isEmpty(asString("itemRef"))) return forward("sagyoItem.jsp");
        Key itemKey = asKey("itemRef");
        SagyoItem si;
        if(itemKey == null) {
            return forward("sagyoItem.jsp");
        } else {
            si = siService.get(itemKey);
        }
        
        Date[] range = Util.createOneYearRange(yearI);
        List<Sagyo> list = sService.getByDateRangeSagyoItem(loginYago, range[0], range[1], si);
        requestScope("list", list);
        
        Map<Sakuduke, Double> map = new HashMap<Sakuduke, Double>();
        for(Sagyo s : list) {
            Sakuduke sakuduke = s.getSakudukeRef().getModel();
            Double sum = s.getAmount();
            if(sum == null) sum = 0d;
            if(map.containsKey(sakuduke)) {
                sum += map.get(sakuduke);
            }
            map.put(sakuduke, sum);
        }
        requestScope("map", map);
        requestScope("si", si);
        
      //文書オブジェクトを生成
        Document doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); 

        //出力先(アウトプットストリーム)の生成
        
        response.setContentType("application/pdf");
        OutputStream os = response.getOutputStream();
        
        //アウトプットストリームをPDFWriterに設定
        PdfWriter pdfwriter = PdfWriter.getInstance(doc, os);
        
        //フォントの設定
        BaseFont baseFont = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        sdf.setTimeZone(TimeZoneLocator.get());
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
        Paragraph p = new Paragraph("農薬・薬剤及び調整用等使用資材管理リスト", new Font(baseFont, 15));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {50,50});
        table.setSpacingBefore(10);

        String dateWide = "";
        if(list.size() > 0) {
            Date startDate = list.get(0).getDate();
            Date endDate = list.get(list.size() - 1).getDate();
            dateWide = "使用期間：" + sdf.format(startDate) + "　〜　" + sdf.format(endDate);
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
        
        table = new PdfPTable(9);
        table.setHeaderRows(2);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {11,14,14,13,14,7,7,14,7});
        table.setSpacingBefore(10);
        
        cell = new PdfPCell(new Paragraph("生産行程管理者", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setColspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("杉本 和也", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setColspan(3);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(paddingSize);
        cell.setColspan(4);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("年月日", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用資材", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("製造者名", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("入手先", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("該当する別表資材名", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("別表区分", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用場所", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("資材使用の理由", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用方法", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        for(Sagyo sagyo : list) {
            
            Hatake hatake = sagyo.getSakudukeRef().getModel().getHatakeRef().getModel();
            
            for(ShiyoShizai shiyoShizai : sagyo.getShiyoShizaiListRef().getModelList()) {
                
                Shizai shizai = shiyoShizai.getShizaiRef().getModel();
                
                // 年月日
                cell = new PdfPCell(new Paragraph(sdf.format(sagyo.getDate()), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 使用資材名
                cell = new PdfPCell(new Paragraph(shizai.getName(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 製造者名
                cell = new PdfPCell(new Paragraph(shizai.getSeizosha(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 入手先
                cell = new PdfPCell(new Paragraph(shizai.getNyushusaki(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 該当する別表資材名
                cell = new PdfPCell(new Paragraph(shizai.getBeppyoName(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 別表区分
                cell = new PdfPCell(new Paragraph(shizai.getBeppyoKubun(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 使用場所
                cell = new PdfPCell(new Paragraph("ほ場（" + hatake.getNo() + "）", new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 資材使用の理由
                cell = new PdfPCell(new Paragraph(shizai.getRiyu(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
                
                // 使用方法
                cell = new PdfPCell(new Paragraph(sagyo.getSagyoItemRef().getModel().getName(), new Font(baseFont, fontSize)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(paddingSize);
                table.addCell(cell);
            }
        }
            
        doc.add(table);
        
        //文章オブジェクト クローズ
        doc.close();
            
        //PDFWriter クローズ
        pdfwriter.close();
        
        return null;
    }
}
