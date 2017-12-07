package myblog.user.info;

import myblog.user.User;

public interface UserInfoService {
    void add (UserInfo userInfo);
    UserInfo createFromUser (User user);
    UserInfo load (String username);
    UserInfo load (Long id);
    void update(UserInfo userInfo);
    String getUserAvatarPath(UserInfo user);
    void  setUserAvatarPath(UserInfo user);
    void setUserRepostsAvatar(UserInfo user);
    UserInfo getActiveUserInfo();
    void toggleSubscribe(UserInfo user, Long requestId);
    void fillSubsWithAvatars(UserInfo user);
    void changeName(UserInfo user, String firstName, String lastName);
}
