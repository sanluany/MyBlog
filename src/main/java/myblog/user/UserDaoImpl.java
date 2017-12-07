package myblog.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager em;

    public void add(User user) {
        em.persist(user);
    }

    public User loadUserByUsername(String username) {
        Query query = em.createQuery("SELECT u from User u where u.username=:username");
        query.setParameter("username", username);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User loadUserByEmail(String email) {
        Query query = em.createQuery("SELECT u from User u where u.email=:email");
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String loadUserRole(String username) {
        String sql = "SELECT Authority FROM authorities WHERE username =?";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,username);
        try{
            return (String) query.getSingleResult();
        } catch (NoResultException e){
            return "anonymousUser";
        }
    }

    @Override
    public void addUserRole(String username, String role) {
        String sql = "INSERT INTO authorities (Username,Authority) VALUES (?,?)";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,username);
        query.setParameter(2,role);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void update(User user) {
        em.merge(user);
    }
}
