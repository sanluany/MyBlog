package myblog.file;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileHandlerImpl implements FileHandler {

    @Override
    public boolean safe(String filename, String image) {
        String UPLOAD_LOCATION = "H:/Java Projects/FTP/img/";
        try {
            FileOutputStream fos = new FileOutputStream(UPLOAD_LOCATION + filename + ".png");
            byte myImage[] = Base64.decodeBase64(image);
            fos.write(myImage);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
