package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.slack.UserList;
import edu.asu.cassess.persist.entity.slack.UserObject;

/**
 * Created by Thomas on 4/22/2017.
 */
public interface IConsumeUsers {
    UserList getUserList(String token);

    void saveUserList(UserList userList, String course);

    void updateSlackUsers(String course);

    void deleteSlackUsers(String course);

    UserObject getUserInfo(String userID, String token);
}
