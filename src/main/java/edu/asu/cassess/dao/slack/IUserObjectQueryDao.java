package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface IUserObjectQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    <T> Object getUserByEmail(String course, String email) throws DataAccessException;

    <T> Object getUserByDisplayName(String course, String display_name) throws DataAccessException;

    <T> Object deleteUserByEmail(String course, String email) throws DataAccessException;

    <T> List<UserObject> getUsersByEmail(String email) throws DataAccessException;

    <T> List<UserObject> getUsersByDisplayName(String display_name) throws DataAccessException;
}
