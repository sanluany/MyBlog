package myblog.post;

import java.util.List;

public interface PostDao {
    void add(Post post);
    List<Post> loadPostsByUsername(String username);
    Post loadPostById(Long id);
    void updatePost(Post post);
    void remove(Long id);
    Long getPostIdByRepostedId (Long repostedId);
    List<Post> getFeed (List<String> authors);
}
