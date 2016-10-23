package agri.controller.hanbai;

import java.io.OutputStream;

import org.slim3.controller.Navigation;

import agri.controller.BaseController;
import agri.model.hanbai.Shukka;
import agri.service.hanbai.ShukkaService;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfNohinController extends BaseController {

    private ShukkaService service = new ShukkaService();
    
    @Override
    public boolean validateAuth() {
        Shukka s = service.get(asKey("key"), asLong("version"));
        if(!loginYago.equals(s.getYagoRef().getModel())) return false;
        return loginUser.isAuthShukkaView();
    }
    
    @Override
    protected Navigation run() throws Exception {
        
        Shukka s = service.get(asKey("key"), asLong("version"));
        
      //文書オブジェクトを生成
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50); 

        //出力先(アウトプットストリーム)の生成
        response.setContentType("application/pdf");
        OutputStream os = response.getOutputStream();
        
        //アウトプットストリームをPDFWriterに設定
        PdfWriter pdfwriter = PdfWriter.getInstance(doc, os);
        
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
        
        service.printNohin(s, doc);
        
        //文章オブジェクト クローズ
        doc.close();
            
        //PDFWriter クローズ
        pdfwriter.close();
        
        return null;
        
    }

}
