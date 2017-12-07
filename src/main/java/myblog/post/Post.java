package myblog.post;

import myblog.user.info.UserInfo;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "AUTHOR")
    private String author;

    @Transient
    private String authorImagePath;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "isRepost")
    private boolean isRepost;

    @Column(name = "repostedId")
    private Long repostedId;

    @ManyToMany
    @JoinTable(name = "post_likes", joinColumns = {@JoinColumn(name = "postId")}
            , inverseJoinColumns = {@JoinColumn(name = "userInfoId")})
    private Set<UserInfo> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "post_reposts", joinColumns = {@JoinColumn(name = "postId")}
            , inverseJoinColumns = {@JoinColumn(name = "userInfoId")})
    private Set<UserInfo> reposts = new HashSet<>();

    @Transient
    private UserInfo originalAuthor; // used whe post is a repost;

    public Post() {
    }

    public Post(String content, String author, Date date) {
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getAuthorImagePath() {
        return authorImagePath;
    }

    public void setAuthorImagePath(String authorImagePath) {
        this.authorImagePath = authorImagePath;
    }

    public Set<UserInfo> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserInfo> likes) {
        this.likes = likes;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return postId == post.postId;

    }

    @Override
    public int hashCode() {
        return (int) (postId ^ (postId >>> 32));
    }

    public boolean isRepost() {
        return isRepost;
    }

    public void setRepost(boolean repost) {
        isRepost = repost;
    }

    public Set<UserInfo> getReposts() {
        return reposts;
    }

    public void setReposts(Set<UserInfo> reposts) {
        this.reposts = reposts;
    }


    public Long getRepostedId() {
        return repostedId;
    }

    public void setRepostedId(Long repostedId) {
        this.repostedId = repostedId;
    }

    public UserInfo getOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(UserInfo originalAuthor) {
        this.originalAuthor = originalAuthor;
    }
}
