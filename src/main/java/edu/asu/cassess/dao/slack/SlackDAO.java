package edu.asu.cassess.dao.slack;

import edu.asu.cassess.dao.CAssessDAO;
import edu.asu.cassess.persist.entity.slack.SlackAuth;
import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class SlackDAO implements ISlackDAO {

    @Autowired
    private CAssessDAO dao;

    @Override
    public UserObject getUserById(String uid) {
        return dao.find(UserObject.class, uid);
    }

    @Override
    public List<UserObject> getAllUsers() {
        return dao.findAll(UserObject.class);
    }

    @Override
    public String getSlackToken() {
        String auth = dao.find(SlackAuth.class, 1).getToken();
        return auth;
    }

    @Override
    public void saveUserList(List<UserObject> userList) {
        dao.save(userList.toArray());
    }
}
