package agri.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slim3.controller.Navigation;
import org.slim3.util.TimeZoneLocator;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.awt.Color;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;


public class InitController extends BaseController {
    
  
    @Override
    public boolean validateAuth() {
        if(!loginUser.getEmail().getEmail().equals("sugimotokazuya@gmail.com")) {
            return false;
        }
        return true;
    }
    
    @Override
    public Navigation run() throws Exception {
        
        
        //String str = asString("year");
        //if(applicationScope("init" + str) == null) {
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("kazuya@haruhibatake.jp"));
        msg.addRecipient(
            Message.RecipientType.TO,
            new InternetAddress("sugimotokazuya@gmail.com"));
 
        msg.setSubject("タイトル", "ISO-2022-JP");
        //msg.setText();
        
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText("本文\n\n２行目\n\n３行目", "ISO-2022-JP");

        
        MimeBodyPart mbp2 = new MimeBodyPart();
        FileDataSource fds = new FileDataSource("test.pdf");
        
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName("test.pdf");
        

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
          //float fontSize = 9;
          
          // ヘッダー
          HeaderFooter header = new HeaderFooter(new Phrase(
              new Chunk("別添様式第６号　（別記様式第１２号１　有機農産物の調査申請書関係）",new Font(baseFont, 10))),false);
          header.setAlignment(Element.ALIGN_LEFT);
          header.setBorderColor(Color.white);
          doc.setHeader(header);
        //文章オブジェクト オープン
          doc.open();
          //int paddingSize = 5;
          
          //タイトル
          Paragraph p = new Paragraph("農薬・薬剤及び調整用等使用資材管理リスト", new Font(baseFont, 15));
          p.setAlignment(Element.ALIGN_CENTER);
          doc.add(p);
        //文章オブジェクト クローズ
          doc.close();
              
          //PDFWriter クローズ
          pdfwriter.close();
          
        


        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);

        msg.setContent(mp);
 
        
        
        // 送信実行
        Transport.send(msg);
            
            //applicationScope("init" + str, "on");
        //}
        return null;
    }
 
}
