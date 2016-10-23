package agri.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import agri.model.ImageEntity;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class ImageUtil {
    
    private static final GcsService gcsService = GcsServiceFactory
            .createGcsService(new RetryParams.Builder().initialRetryDelayMillis(10)
                .retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());
    
    private static String bucketName = Const.BUCKET_NAME;

    public static long putImage(String fileName, String contentType, byte[] bytes) throws Exception {
        
        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(new GcsFilename(bucketName, fileName),
                        new GcsFileOptions.Builder().mimeType(contentType).build());
        try (OutputStream outputStream = Channels.newOutputStream(outputChannel)) {;
            outputStream.write(bytes);
        }
        
        return 0;
     }
     
     public static ImageEntity getImageEntity(Long imageId) {
         
         PersistenceManager pm = PMF.get().getPersistenceManager();
         Query query = pm.newQuery(ImageEntity.class);
         
         query.setFilter("id == imageId");
         query.declareParameters("Long imageId");
     
         @SuppressWarnings("unchecked")
        List<ImageEntity> images = (List<ImageEntity>) query.execute(new Long(imageId));    

     
         if (!images.isEmpty()) 
             return images.get(0); 
         else
             return null;
     }
     
     public static byte[] readBytes(InputStream is) throws IOException {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         int len;
         byte[] buf = new byte[10 * 1024];
         while ((len = is.read(buf)) >= 0) {
             bos.write(buf, 0, len);
         }
         return bos.toByteArray();
     }
     
     public static Blob createThumbnail( byte[] data, int size ) {
         
         ImagesService imagesService = ImagesServiceFactory.getImagesService();
         Image image = ImagesServiceFactory.makeImage(data);
         
         float width = 0;
         float height = 0;
         if(image.getWidth() > image.getHeight()) {
             width = size;
             height = image.getHeight() * size / width;
         } else {
             width = image.getWidth() * size / height;
             height = size;
         }
         
         // 縦横の比率を保ったまま短辺にあわせてリサイズ後、中心部をトリミング
         List<Transform> ts = new ArrayList<Transform>();
         ts.add(ImagesServiceFactory.makeResize((int) width, (int) height));
        
         Transform t = ImagesServiceFactory.makeCompositeTransform(ts);
         Image thumbnail = imagesService.applyTransform(t, image);
         return new Blob(thumbnail.getImageData());
     }


}
