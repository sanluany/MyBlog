package myblog.file;

import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private  UserInfoService userInfoService;
    @Autowired
    private  FileHandler fileHandler;

    @Override
    public void safe(String image, UserInfo user) {
        String filename = generateFilename();
        image = image.substring(image.indexOf(",") + 1);
        if (fileHandler.safe(filename, image)) {
            user.setAvatarImage(filename);
            userInfoService.update(user);
        }
    }

    private String generateFilename() {
        return String.valueOf(UUID.randomUUID());
    }
}
