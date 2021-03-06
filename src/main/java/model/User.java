package model;

import javax.enterprise.inject.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity @Model
@NamedQueries({
        @NamedQuery(name="user.findByName",
                query="SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name="user.findUsersByName",
                query="SELECT u FROM User u WHERE u.username LIKE :username")
})
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type;
    private String bio;
    private String location;
    private String avatarURL;
    private String websiteURL;
    private String username;
    @OneToMany(fetch = FetchType.EAGER) @JoinTable(name = "USER_FOLLOWING")
    private List<User> following;
    @OneToMany(fetch = FetchType.EAGER) @JoinTable(name = "USER_FOLLOWERS")
    private List<User> followers;
    @OneToMany(mappedBy = "createdBy")
    private List<Kweet> kweets;
    private String password;
    @ManyToMany(mappedBy = "users")
    private List<UserGroup> groups;

    public User() {
        this.avatarURL = "http://s3.amazonaws.com/37assets/svn/765-default-avatar.png";
        this.kweets = new ArrayList<Kweet>();
        this.following = new ArrayList<User>();
        this.followers = new ArrayList<User>();
    }

    public void addTweet(Kweet kweet){
        kweets.add(kweet);
    }

    public void follow(User user){
        if(!this.following.contains(user)){
            user.addFollower(this);
            this.following.add(user);
        }
    }

    public void unfollow(User userToUnfollow) {
        if (this.following.contains(userToUnfollow)) {
            userToUnfollow.removeFollower(this);
            this.following.remove(userToUnfollow);
        }
    }

    private void addFollower(User user){
        if(!this.followers.contains(user))
            this.followers.add(user);
    }

    private void removeFollower(User user) {
        if (this.followers.contains(user)){
            this.followers.remove(user);
        }
    }

    public List<Kweet> getRecentKweets(int offset, int limit){
        ArrayList<Kweet> result = new ArrayList<Kweet>(getKweets());

        Collections.sort(result);
        if (result.size() >= offset + limit){
            return result.subList(offset, offset + limit);
        } else {
            return result.subList(offset, result.size());
        }
    }

    public void likeKweet(Kweet kweet){
        kweet.addLike(this);
    }

    public List<Kweet> getTimelineKweets(int offset, int limit){
        ArrayList<Kweet> result = new ArrayList<Kweet>(getKweets());
        for (User user: following) {
            result.addAll(user.getKweets());
        }

        Collections.sort(result);
        if (result.size() >= offset + limit){
            return result.subList(offset, offset + limit);
        } else {
            return result.subList(offset, result.size());
        }
    }

    public boolean removeKweet(Kweet kweet){
        if (this.kweets.contains(kweet))
            return kweets.remove(kweet);
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public boolean setBio(String bio) {
        this.bio = bio;
        return true;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public boolean setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
        return true;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public boolean setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
        return true;
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        this.username = username;
        return true;
    }

    public boolean setLocation(String location) {
        this.location = location;
        return true;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<Kweet> getKweets() {
        return kweets;
    }

    public String getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
