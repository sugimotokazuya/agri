package agri.controller.gazo;

import java.io.OutputStream;
import java.nio.channels.Channels;

import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;

import agri.controller.BaseController;
import agri.model.sagyo.Gazo;
import agri.service.Util;
import agri.service.sagyo.GazoService;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class TestGazoController extends BaseController {

    private GazoService gazoService = new GazoService();
    private final GcsService gcsService = GcsServiceFactory
            .createGcsService(new RetryParams.Builder().initialRetryDelayMillis(10)
                .retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());
    
    @Override
    public boolean validateAuth() {
        String env = System.getProperty("com.google.appengine.runtime.environment");
        return "Development".equals(env);
    }
    
    @Override
    public Navigation run() throws Exception {
        
        String bucketName = AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName();
        requestScope("bucketName", bucketName);
        
        
        
        FileItem fileItem = requestScope("formFile");
        if(fileItem == null) return forward("testGazo.jsp");

        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(new GcsFilename(bucketName, "test"),
                        new GcsFileOptions.Builder().mimeType(fileItem.getContentType()).build());
        try (OutputStream outputStream = Channels.newOutputStream(outputChannel)) {
            outputStream.write(fileItem.getData());
        }
        
        Gazo gazo = new Gazo();
        gazo.getUserRef().setModel(loginUser);
        gazo.setDate(Util.getCalendar().getTime());
        gazoService.insert(fileItem.getContentType(), fileItem.getData(), gazo);
        
        return forward("testGazo.jsp");
    }
    
    
}
