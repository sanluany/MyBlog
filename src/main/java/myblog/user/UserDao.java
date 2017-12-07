package myblog.user;

public interface UserDao {
    void add(User user);
    User loadUserByUsername (String username);
    User loadUserByEmail (String email);
    String loadUserRole (String username);
    void  addUserRole (String username, String role);
    void update(User user);
}
