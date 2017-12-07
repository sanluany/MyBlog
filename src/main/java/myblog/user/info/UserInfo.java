package myblog.user.info;


import myblog.post.Post;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Transactional
@Entity
@Table(name = "User_Info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInfoId;

    @Column(name = "USERNAME")
    private String username;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "AVATAR_IMAGE")
    private String avatarImage;
    @Transient
    private String avatarImagePath;

    @Transient
    private Long subscribeRequestId;

    @Transient
    private boolean isSubscribed;

    @ManyToMany()
    @JoinTable(name = "subs", joinColumns = {@JoinColumn(name = "userInfoId")},
            inverseJoinColumns = {@JoinColumn(name = "subscriptionId")})
    private Set<UserInfo> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    private Set<UserInfo> subscribers;

    @ManyToMany(mappedBy = "likes")
    private Set<Post> liked = new HashSet<>();

    @ManyToMany(mappedBy = "reposts")
    private Set<Post> reposted = new HashSet<>();

    @Transient
    private Post repost;

    public UserInfo() {
    }

    public UserInfo(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarImagePath() {
        return avatarImagePath;
    }

    public void setAvatarImagePath(String avatarImagePath) {
        this.avatarImagePath = avatarImagePath;
    }


    public Set<Post> getLiked() {
        return liked;
    }

    public void setLiked(Set<Post> liked) {
        this.liked = liked;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        return username.equals(userInfo.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }


    public Set<Post> getReposted() {
        return reposted;
    }

    public void setReposted(Set<Post> reposted) {
        this.reposted = reposted;
    }

    public Post getRepost() {
        return repost;
    }

    public void setRepost(Post repost) {
        this.repost = repost;
    }

    public void setRepostById(String id) {
        if (id != null) {
            for (Post post : reposted) {
                if (post.getPostId() == Long.valueOf(id)) {
                    repost = post;
                }
            }
        }
    }

    public Set<UserInfo> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<UserInfo> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Long getSubscribeRequestId() {
        return subscribeRequestId;
    }

    public void setSubscribeRequestId(Long subscribeRequestId) {
        this.subscribeRequestId = subscribeRequestId;
    }

    public boolean isSubscribed(UserInfo user) {
        return subscriptions.contains(user);
    }


    public Set<UserInfo> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<UserInfo> subscribers) {
        this.subscribers = subscribers;
    }
}
