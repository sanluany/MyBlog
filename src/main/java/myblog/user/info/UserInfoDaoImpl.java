package myblog.user.info;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {

    @PersistenceContext
    private EntityManager em;

    public void add(UserInfo userInfo) {
        em.persist(userInfo);
    }

    @Override
    public UserInfo load(String username) {
        Query query = em.createQuery("SELECT u from UserInfo u where u.username=:username");
        query.setParameter("username", username);
        try {
            return (UserInfo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserInfo load(Long id) {
        Query query = em.createQuery("SELECT u from UserInfo u where u.userInfoId=:id");
        query.setParameter("id", id);
        try {
            return (UserInfo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void update(UserInfo userInfo) {
        em.merge(userInfo);
    }
}
