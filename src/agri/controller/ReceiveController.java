package agri.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import agri.model.Torihikisaki;
import agri.model.User;
import agri.model.Yago;
import agri.model.mail.RecMail;
import agri.model.sagyo.Gazo;
import agri.service.ImageUtil;
import agri.service.TorihikisakiService;
import agri.service.UserService;
import agri.service.Util;
import agri.service.YagoService;
import agri.service.mail.RecMailService;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class ReceiveController extends Controller {

    private RecMailService service = new RecMailService();
    private YagoService yagoService = new YagoService();
    private UserService userService = new UserService();
    private GazoService gazoService = new GazoService();
    private TorihikisakiService tService = new TorihikisakiService();
    
    @Override
    public Navigation run() throws Exception {
        
        if(this.isGet()) return null;
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        MimeMessage message = new MimeMessage(session, request.getInputStream());

        String addressStr = asString("address");
        System.out.println(addressStr);
        String localAddr = addressStr.split("@")[0];

        // ユーザーのKeyから処理する
        if(localAddr.startsWith("photo")) {
            try {
                long id = Long.parseLong(localAddr.substring(5));
                Key userKey = KeyFactory.createKey("User", id);
                User user = userService.get(userKey);
                String from = message.getFrom()[0].toString();
                InternetAddress ia = new InternetAddress(from);
                from = ia.getAddress();
                
                if(user == null) {
                    System.out.println("Illegal to");
                    return null;
                }
                System.out.println("from:" + from);
                if(!from.equals(user.getEmailStr()) && !from.equals(user.getEmail2Str())) {
                    System.out.println("Illeal from");
                    return null;
                }
                
                exeGazoFile(user, message);
                return null;
            
            } catch(NumberFormatException ex) {
                System.out.println("id is illegal.");
            }
        }
        
        if(localAddr.startsWith("uriage")) {
            long id = Long.parseLong(localAddr.substring(6));
            Key tKey = KeyFactory.createKey("Torihikisaki", id);
            Torihikisaki t = tService.get(tKey);
            exeUriageMail(t.getYagoRef().getModel(), message);
            return null;
        }
        
        //以前の仕様用
        Key key = KeyFactory.stringToKey(localAddr);
        if("Yago".equals(key.getKind())) {
            Yago yago = yagoService.get(key);
            exeUriageMail(yago, message);
            return null;
        }
        
        return null;
    }

    // 画像ファイル登録
    private void exeGazoFile(User user, Part msg) throws Exception {
        
        System.out.println("ContentType:" + msg.getContentType());
        
        if (msg.getContentType().startsWith("text/")) { 
            return;
        }
        if (msg.getContentType().startsWith("image/")) {
            Gazo gazo = new Gazo();
            gazo.getUserRef().setModel(user);
            gazo.setDate(Util.getCalendar().getTime());
            byte[] bytes = ImageUtil.readBytes((InputStream) msg.getContent());
            gazoService.insert(msg.getContentType(), bytes, gazo);
            // 下のメソッドで応答が返ってこない（撮影日が欲しかったのに）
            //Metadata metadata = ImageMetadataReader.readMetadata((InputStream)o);
            //Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
            //gazo.setDate(directory.getDate(ExifIFD0Directory.TAG_DATETIME));
            return;
        }
        if (msg.getContentType().startsWith("multipart/")) {
            // マルチパートをそれぞれ再起的に処理
            Multipart mp = (Multipart)msg.getContent();
            for (int i = 0; i < mp.getCount(); i++)
                exeGazoFile(user, mp.getBodyPart(i));
            return;
        }
        throw new IOException("unexpected content: '" + 
                              msg.getContentType() + "'");
        
    }
    
    // 売上メール受信
    private void exeUriageMail(Yago yago, MimeMessage message) throws Exception {
        try {
            RecMail mail = new RecMail();
            mail.getYagoRef().setModel(yago);
            String env = System.getProperty("com.google.appengine.runtime.environment");
            if("Development".equals(env)) {
                // 開発環境ではこれが無いと文字化け、本番環境ではこれすると文字化け
                mail.setSubject(new String(message.getSubject().getBytes("8859_1"), "UTF-8"));
            } else {
                mail.setSubject(message.getSubject());
            }
            mail.setFrom(message.getFrom()[0].toString());
            String contentType = message.getContentType();
            InputStream is = null;
            String mess = "";
    
            if(message.isMimeType("text/plain")){
                mess = (String)message.getContent();
            }else{
                Multipart content = (Multipart)message.getContent();
    
                for(int i = 0; i < content.getCount(); ++i){
                    BodyPart bp = content.getBodyPart(i);
                    if(!bp.isMimeType("text/plain")) continue;
                    is = bp.getInputStream();
    
                    contentType = bp.getContentType();
                    break;
                }
            }
            if(is != null){
                //contentTypeからエンコーディングを取得
                String encoding = null;
                String[] elms = contentType.split(";");
                for(String elm : elms){
                    if(elm.trim().startsWith("charset=")){
                        encoding = elm.trim().substring("charset=".length());
                    }
                }
    
                Reader r = null;
                if(encoding != null){
                    //エンコーディングが入っている
                    if(encoding.startsWith("\"")) encoding = encoding.substring(1);
                    if(encoding.endsWith("\"")) encoding = 
                        encoding.substring(0, encoding.length() - 1);
                    r = new InputStreamReader(is, encoding);
                }else{
                    //エンコーディングが入っていない
                    r = new InputStreamReader(is);
                }
    
                //2009/12/11 挙動がかわったことに対応
                //String mess = "";
                BufferedReader buf = new BufferedReader(r);
                for(String line; (line = buf.readLine()) != null;){
                    mess += line + "\n";
                }
            }
            mail.setText(mess);
            mail.setDate(Util.getCalendar().getTime());
            
            Transaction tx = Datastore.beginTransaction();
            
            System.out.println(mail.getText());
            
            Datastore.put(tx, mail);
            tx.commit();
            String subject = mail.getSubject();
            Calendar cal = Util.getCalendar();

            String h = subject.substring(11, 13);
            String m = subject.substring(14, 16);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(h));
            cal.set(Calendar.MINUTE, Integer.valueOf(m));
           if(subject.length() > 18) {
               // 手動用
               cal.set(Calendar.YEAR, Integer.valueOf(subject.substring(18, 22)));
               cal.set(Calendar.MONTH, Integer.valueOf(subject.substring(22, 24)) - 1);
               cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(subject.substring(24, 26)));
           }
            service.SunshinMail(yago, cal.getTime(), mess);
            
        } catch (MessagingException ex) {
            throw new ServletException(ex);
        }finally{
            
        }
     
    }
 
}
