package myblog.user;

import myblog.misc.PasswordHolder;

public interface UserService {
    void add(User user);
    User loadUserByUsername(String name);
    User loadUserByEmail (String email);
    String hashPassword(String password);
    String loadUserRole (String username);
    void addUserRole (String username);
    void changePassword(PasswordHolder passwordHolder, User user) throws Exception;
    User getActiveUser();
}
