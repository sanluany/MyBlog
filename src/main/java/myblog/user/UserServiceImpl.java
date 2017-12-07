package myblog.user;

import myblog.misc.PasswordHolder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserDao userDao;

    public void add(User user) {
        String password = user.getPassword();
        user.setPassword(hashPassword(password));
        userDao.add(user);

    }

    public User loadUserByUsername(String name) {
        return userDao.loadUserByUsername(name);
    }

    public User loadUserByEmail(String email) {

        return userDao.loadUserByEmail(email);
    }


    public String hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    public String loadUserRole(String username) {
        return userDao.loadUserRole(username);
    }

    @Override
    public void addUserRole(String username) {
        userDao.addUserRole(username, "USER");
    }

    @Override
    public void changePassword(PasswordHolder passwordHolder, User user) throws Exception {
            if(BCrypt.checkpw(passwordHolder.getPreviousPassword(),user.getPassword())){
                user.setPassword(hashPassword(passwordHolder.getNewPassword()));
                userDao.update(user);
            } else {
                throw new Exception("Previous and new passwords do not match");
            }
    }

    @Override
    public User getActiveUser() {
        return userDao.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }


}
