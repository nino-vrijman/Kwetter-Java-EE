package dao;

import model.Kweet;
import model.User;

import java.util.List;

/**
 * Created by Nino Vrijman.
 */
public interface UserDao {

    User addUser(User user);

    List<User> getUsers();

    User getUserByUsername(String username);

    List<Kweet> getRecentKweets(User user, int offset, int limit);

    List<User> getFollowers(User user);

    List<User> getFollowing(User user);

    List<Kweet> getTimelineKweets(User user, int offset, int limit);

    void update(User user);
}
