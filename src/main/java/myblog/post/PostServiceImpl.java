package myblog.post;

import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private  PostDao postDao;
    @Autowired
    private  UserInfoService userInfoService;

    @Override
    public void add(Post post, UserInfo author) {
        post.setAuthor(author.getUsername());
        post.setDate(new Date());
        postDao.add(post);
    }

    @Override
    public void removePost(UserInfo user, Long postId) {
        Post post = loadPostById(postId);
        post.getReposts().clear();
        post.getLikes().clear();
        if (post.isRepost()) {
            Post parentPost = loadPostById(post.getRepostedId());
            parentPost.getReposts().remove(user);
            postDao.updatePost(parentPost);
        }
        postDao.updatePost(post);
        postDao.remove(postId);
    }

    @Override
    public List<Post> loadPostsByUsername(String username) {
        List<Post> posts = postDao.loadPostsByUsername(username);
        UserInfo author = userInfoService.load(username);
        if (posts != null) {
            posts = fillPostsWithAvatars(posts, author);
            return posts;
        } else {
            return null;
        }
    }

    @Override
    public Post loadPostById(Long id) {
        return postDao.loadPostById(id);
    }

    @Override
    public void toggleAction(Long postId, UserInfo user, boolean isLike) {
        Post post = postDao.loadPostById(postId);
        Set<UserInfo> set = getUserInfoByAction(isLike, post);
        toggleSetWithUser(set, user);
        postDao.updatePost(post);
        if (!isLike) {
            doRepost(user, post);
        }
    }

    private void doRepost(UserInfo user, Post post) {
        if (user.getReposted().contains(post)) {
            addRepost(user, post.getPostId());
        } else {
            removePost(user, postDao.getPostIdByRepostedId(post.getPostId()));
        }
    }

    private void toggleSetWithUser(Set<UserInfo> set, UserInfo user) {
        if (!set.contains(user)) {
            set.add(user);
        } else {
            set.remove(user);
        }
    }

    @Override
    public Post getPostById(Long id, Set<Post> posts) {
        for (Post post : posts) {
            if (post.getPostId() == id) {
                return post;
            }
        }
        return null;
    }

    @Override
    public List<Post> fillPostsWithAvatars(List<Post> posts, UserInfo author) {
        for (Post post : posts) {
            post.setAuthorImagePath(userInfoService.getUserAvatarPath(author));
        }
        return posts;
    }

    @Override
    public List<Post> fillPostsWithAvatars(List<Post> posts) {
        List<UserInfo> authors = new ArrayList<>();
        for (Post post : posts) {
            UserInfo author;
            if (!authors.contains(post.getAuthor())) {
                author = userInfoService.load(post.getAuthor());
                authors.add(author);
            } else {
                author = authors.get(authors.indexOf(post.getAuthor()));
            }
            post.setAuthorImagePath(userInfoService.getUserAvatarPath(author));
            userInfoService.setUserRepostsAvatar(author);
            post.setOriginalAuthor(author);
        }
        return posts;
    }

    @Override
    public Post fillPostWithAvatar(Post post, UserInfo author) {
        post.setAuthorImagePath(userInfoService.getUserAvatarPath(author));
        return post;
    }

    @Override
    public List<Post> getFeed(UserInfo user) {
        int subSize = user.getSubscriptions().size();
        if (subSize > 0) {
            List<String> authors = new ArrayList<>(subSize);
            for (UserInfo userInfo : user.getSubscriptions()) {
                authors.add(userInfo.getUsername());
            }
            List<Post> posts = postDao.getFeed(authors);
            posts = fillPostsWithAvatars(posts);
            return posts;
        }
        return null;
    }


    private void addRepost(UserInfo user, Long postId) {
        Post post = new Post();
        post.setRepost(true);
        post.setRepostedId(postId);
        add(post, user);
    }

    private Set<UserInfo> getUserInfoByAction(boolean isLike, Post post) {
        if (isLike) {
            return post.getLikes();
        } else {
            return post.getReposts();
        }
    }

}
