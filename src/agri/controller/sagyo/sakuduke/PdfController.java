package agri.controller.sagyo.sakuduke;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.TimeZoneLocator;

import agri.controller.BaseController;
import agri.model.Hatake;
import agri.model.sagyo.Kikai;
import agri.model.sagyo.Sagyo;
import agri.model.sagyo.Sakuduke;
import agri.model.sagyo.ShiyoKikai;
import agri.model.sagyo.ShiyoShizai;
import agri.model.sagyo.Shizai;
import agri.service.Const;
import agri.service.Util;
import agri.service.sagyo.SakudukeService;

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

public class PdfController extends BaseController {

    private SakudukeService service = new SakudukeService();

    @Override
    public boolean validateAuth() {
        return loginUser.isAuthSakudukeView();
    }
    
    @Override
    public Navigation run() throws Exception {
        
        if (!validate()) {
            return forward("kiroku");
        }
        
        Key key = asKey("key");
        Sakuduke sakuduke = service.get(key);
        Calendar cal = Util.getCalendar();
        Util.resetTime(cal);
        cal.set(asInteger("year"), asInteger("month") - 1, asInteger("day"));
        sessionScope(KirokuController.YEAR, asInteger("year"));
        sessionScope(KirokuController.MONTH, asInteger("month"));
        sessionScope(KirokuController.DAY, asInteger("day"));
        List<Sagyo> list = sakuduke.getSagyoListRef().getModelList();
        Hatake hatake = sakuduke.getHatakeRef().getModel();
        
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
        DecimalFormat df = new DecimalFormat("#,###");
        
        // ヘッダー
        HeaderFooter header = new HeaderFooter(new Phrase(
            new Chunk("添様式第２号２　（別記様式第４号１　有機農産物の認定申請書関係）",new Font(baseFont, 10))),false);
        header.setAlignment(Element.ALIGN_LEFT);
        header.setBorderColor(Color.white);
        doc.setHeader(header);
        
        //　フッター
        StringBuilder sb = new StringBuilder();
        sb.append(hatake.getName());
        sb.append(":");
        sb.append(sakuduke.getName());
        sb.append("（開始日：");
        sb.append(sdf.format(sakuduke.getStartDate()));
        sb.append("）");
        HeaderFooter footer = new HeaderFooter(new Phrase(
            new Chunk(sb.toString(),new Font(baseFont, 10))),false);
        footer.setAlignment(Element.ALIGN_LEFT);
        footer.setBorderColor(Color.white);
        doc.setFooter(footer);
        
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
        
        //タイトル
        Paragraph p = new Paragraph("圃場別生産工程管理記録（過去２年間）", new Font(baseFont, 15));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {33,34,33});
        table.setSpacingBefore(10);

        String dateWide = "";
        if(list.size() > 0) {
            Date startDate = list.get(0).getDate();
            Date endDate = list.get(list.size() - 1).getDate();
            dateWide = sdf.format(startDate) + "　〜　" + sdf.format(endDate);
        }
        PdfPCell cell = new PdfPCell(new Paragraph(dateWide, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("生産工程管理記録", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        Calendar now = Util.getCalendar();
        cell = new PdfPCell(new Paragraph("作成　" + sdf.format(now.getTime()), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        doc.add(table);
        
        table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {7,33,7,20,16,16});
        table.setSpacingBefore(5);
        int paddingSize = 5;
        
        cell = new PdfPCell(new Paragraph("圃場番号", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("所　在　地", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("面積　㎡", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("作物名（種別）", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("生　産　者　名", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("有機開始年月日", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(String.valueOf(hatake.getNo()), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(hatake.getAddress(), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(df.format(sakuduke.getArea() * 100), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph(sakuduke.getName(), new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        String adminUserStr = sakuduke.getAdminUserRef().getModel() != null ? sakuduke.getAdminUserRef().getModel().getName() : "";
        cell = new PdfPCell(new Paragraph(adminUserStr, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        String startYuki = "";
        if(hatake.getStartYuhki() != null) {
            startYuki = sdf.format(hatake.getStartYuhki());
        }
        cell = new PdfPCell(new Paragraph(startYuki, new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        
        doc.add(table);
        
        table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidths(new int[] {12,11,14,7,9,12,10,25});
        table.setSpacingBefore(10);
        
        cell = new PdfPCell(new Paragraph("年 月 日", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("作　業　内　容", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用種苗・資材", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setColspan(3);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用機械・器具", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setColspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("特記事項", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        cell.setRowspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("種苗及び使用資材名", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("数　量", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("入 手 先", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("使用機械・器具", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("汚染防止措置", new Font(baseFont, 10)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(paddingSize);
        table.addCell(cell);
        
        for(Sagyo sagyo : list) {
            
            Calendar sagyoCal = Util.getCalendar();
            sagyoCal.setTime(sagyo.getDate());
            if(sagyoCal.getTime().before(cal.getTime())) continue;
            
            int rowspan = 1;
            List<ShiyoShizai> shizaiList = sagyo.getShiyoShizaiListRef().getModelList();
            int shizaiSize = shizaiList.size();
            List<ShiyoKikai> kikaiList = sagyo.getShiyoKikaiListRef().getModelList();
            int kikaiSize = kikaiList.size();
            if(rowspan < shizaiSize) rowspan = shizaiSize;
            if(rowspan < kikaiSize) rowspan = kikaiSize;
            
            cell = new PdfPCell(new Paragraph(sdf.format(sagyo.getDate()), new Font(baseFont, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(paddingSize);
            cell.setRowspan(rowspan);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph(sagyo.getSagyoItemRef().getModel().getName(), new Font(baseFont, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            cell.setRowspan(rowspan);
            table.addCell(cell);
            
            PdfPCell[][] shizaiCells = new PdfPCell[shizaiSize][3];
            for(int i = 0; i < shizaiSize; ++i) {
                Shizai shizai = shizaiList.get(i).getShizaiRef().getModel();
                shizaiCells[i][0] = new PdfPCell(new Paragraph(shizai.getName(), new Font(baseFont, 10)));
                shizaiCells[i][0].setHorizontalAlignment(Element.ALIGN_LEFT);
                shizaiCells[i][0].setPadding(paddingSize);
                shizaiCells[i][1] = new PdfPCell(new Paragraph(String.valueOf(shizaiList.get(i).getAmount())
                    + " " + shizai.getTanni(), new Font(baseFont, 10)));
                shizaiCells[i][1].setHorizontalAlignment(Element.ALIGN_RIGHT);
                shizaiCells[i][1].setPadding(paddingSize);
                shizaiCells[i][2] = new PdfPCell(new Paragraph(shizai.getNyushusaki(), new Font(baseFont, 10)));
                shizaiCells[i][2].setHorizontalAlignment(Element.ALIGN_LEFT);
                shizaiCells[i][2].setPadding(paddingSize);
                
            }
            if(shizaiSize > 0) {
                table.addCell(shizaiCells[0][0]);
                table.addCell(shizaiCells[0][1]);
                table.addCell(shizaiCells[0][2]);
            } else {
                addEmptyCell(table, 3);
            }
            
            PdfPCell[][] kikaiCells = new PdfPCell[kikaiSize][2];
            for(int i = 0; i < kikaiSize; ++i) {
                Kikai kikai = kikaiList.get(i).getKikaiRef().getModel();
                kikaiCells[i][0] = new PdfPCell(new Paragraph(kikai.getName(), new Font(baseFont, 10)));
                kikaiCells[i][0].setHorizontalAlignment(Element.ALIGN_LEFT);
                kikaiCells[i][0].setPadding(paddingSize);
                kikaiCells[i][1] = new PdfPCell(new Paragraph(kikai.getOsenboushisochi(), new Font(baseFont, 10)));
                kikaiCells[i][1].setHorizontalAlignment(Element.ALIGN_LEFT);
                kikaiCells[i][1].setPadding(paddingSize);
            }
            if(kikaiSize > 0) {
                table.addCell(kikaiCells[0][0]);
                table.addCell(kikaiCells[0][1]);
            } else {
                addEmptyCell(table, 2);
            }
            
            cell = new PdfPCell(new Paragraph(sagyo.getBiko(), new Font(baseFont, 10)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(paddingSize);
            cell.setRowspan(rowspan);
            table.addCell(cell);
            
            for(int i = 1; i < rowspan; ++i) {
                if(i < shizaiSize) {
                    for(int j = 0; j < 3; ++j) {
                        table.addCell(shizaiCells[i][j]);
                    }
                } else {
                    addEmptyCell(table, 3);
                }
                if(i < kikaiSize) {
                    for(int j = 0; j < 2; ++j) {
                        table.addCell(kikaiCells[i][j]);
                    }
                } else {
                    addEmptyCell(table, 2);
                }
            }
        }
        
        doc.add(table);
        
        p = new Paragraph("　", new Font(baseFont, 10));
        p.setAlignment(Element.ALIGN_LEFT);
        doc.add(p);
        p = new Paragraph("◎　圃場別、作物品目別に記載してください。", new Font(baseFont, 10));
        p.setAlignment(Element.ALIGN_LEFT);
        doc.add(p);
        p = new Paragraph("◎　有機開始年月日は、慣行栽培農産物の収穫後、有機的管理を開始した時点を示す。", new Font(baseFont, 10));
        p.setAlignment(Element.ALIGN_LEFT);
        doc.add(p);
        p = new Paragraph("◎　汚染防止措置とは、特に機械・器具等の使用前（有機圃場で使用する前）に汚染防止の為の措置。（水で洗浄等。）", new Font(baseFont, 10));
        p.setAlignment(Element.ALIGN_LEFT);
        doc.add(p);
        
        //文章オブジェクト クローズ
        doc.close();
            
        //PDFWriter クローズ
        pdfwriter.close();
        
        return null;
    }
    
    private void addEmptyCell(PdfPTable table, int count) {
        for(int i = 0; i < count; ++i) {
            PdfPCell cell = new PdfPCell(new Paragraph(""));
            table.addCell(cell);
        }
    }
    
    protected boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required(), v.maxlength(Const.KEY_LENGTH));
        v.add("year", v.required(), v.maxlength(4), v.longRange(1900, 2999));
        v.add("month", v.required(), v.longRange(1, 12));
        v.add("day", v.required(), v.longRange(1, 31));
        return v.validate();
    }
}
