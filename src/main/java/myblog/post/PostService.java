package myblog.post;

import myblog.user.info.UserInfo;

import java.util.List;
import java.util.Set;

public interface PostService {
    void add(Post post, UserInfo author);
    void removePost(UserInfo user, Long postId);
    List<Post> loadPostsByUsername(String username);
    Post loadPostById(Long id);
    void toggleAction(Long postId, UserInfo user, boolean isLike);
    Post getPostById(Long id, Set<Post> posts);
    List<Post> fillPostsWithAvatars(List<Post> posts, UserInfo author);
    List<Post> fillPostsWithAvatars(List<Post> posts);
    Post fillPostWithAvatar(Post post, UserInfo author);
    List<Post> getFeed (UserInfo user);
}
