package myblog.user.info;

public interface UserInfoDao {
    void add(UserInfo userInfo);
    UserInfo load (String username);
    UserInfo load (Long id);
    void update(UserInfo userInfo);
}
