package myblog.user.info;

import myblog.post.Post;
import myblog.post.PostService;
import myblog.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private  UserInfoDao userInfoDao;
    @Autowired
    private  PostService postService;

    @Transactional
    public void add(UserInfo userInfo) {
        userInfoDao.add(userInfo);
    }

    public UserInfo createFromUser(User user) {
        String username = user.getUsername().toLowerCase();
        username = StringUtils.capitalize(username);
        return new UserInfo(username, user.getEmail());
    }

    @Override
    public UserInfo load(String username) {
        return userInfoDao.load(username);
    }

    @Override
    public UserInfo load(Long id) {
        return userInfoDao.load(id);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

    @Override
    public String getUserAvatarPath(UserInfo user) {
        if (user.getAvatarImage() != null)
            return "/img/" + user.getAvatarImage() + ".png";
        else
            return "resources/img/defaultAvatar.png";
    }

    @Override
    public void setUserAvatarPath(UserInfo user) {
        if (user.getAvatarImage() != null)
            user.setAvatarImagePath("/img/" + user.getAvatarImage() + ".png");
        else
            user.setAvatarImagePath("resources/img/defaultAvatar.png");
    }

    @Override
    public void setUserRepostsAvatar(UserInfo user) {
        List<Post> posts = new ArrayList<>(user.getReposted());
        for (Post post : posts
                ) {
            postService.fillPostWithAvatar(post, load(post.getAuthor()));
        }
    }

    @Override
    public UserInfo getActiveUserInfo() {
        return userInfoDao.load(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public void toggleSubscribe(UserInfo user, Long requestId) {
        UserInfo requsetedUser = load(requestId);
        if(user.getSubscriptions().contains(requsetedUser)){
            user.getSubscriptions().remove(requsetedUser);
        }else{
            user.getSubscriptions().add(requsetedUser);
        }
        userInfoDao.update(user);
    }

    @Override
    public void fillSubsWithAvatars(UserInfo user) {
        if (user.getSubscriptions() != null) {
            for (UserInfo userInfo : user.getSubscriptions()) {
                setUserAvatarPath(userInfo);
            }
        }
        if (user.getSubscribers() != null) {
            for (UserInfo userInfo : user.getSubscribers()) {
                setUserAvatarPath(userInfo);
            }
        }
    }

    @Override
    public void changeName(UserInfo user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userInfoDao.update(user);
    }
}

