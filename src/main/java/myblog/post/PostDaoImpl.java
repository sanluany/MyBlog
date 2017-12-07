package myblog.post;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void add(Post post) {

        em.persist(post);
    }

    @Override
    public List<Post> loadPostsByUsername(String username) {
        Query query = em.createQuery("SELECT p FROM Post p where p.author=:username order by p.postId desc ");
        query.setParameter("username", username);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Post loadPostById(Long id) {
        Query query = em.createQuery("SELECT p FROM Post p where p.postId=:postId");
        query.setParameter("postId", id);
        try {
            return (Post) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        em.merge(post);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Post post = em.find(Post.class, id);
        em.remove(post);
        em.flush();
    }

    @Override
    public Long getPostIdByRepostedId(Long id) {
        Query query = em.createQuery("SELECT p.postId from Post p where p.repostedId=:id");
        query.setParameter("id", id);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Post> getFeed(List<String> authors) {
        Query query = em.createQuery("select p FROM Post p where p.author IN (:authors) order by p.postId desc");
        query.setParameter("authors",authors);
        try {
           return query.getResultList();
        } catch (NoResultException e){
            return null;
        }
    }


}
