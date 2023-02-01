package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.slack.UserObject;

import java.util.List;

public interface ISlackDAO {

    UserObject getUserById(String uid);

    List<UserObject> getAllUsers();

    String getSlackToken();

    void saveUserList(List<UserObject> userList);

}
