package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class UserObjectQueryDao implements IUserObjectQueryDao {

        protected EntityManager entityManager;

        @Override
        public EntityManager getEntityManager() {
            return entityManager;
        }

        @Override
        @PersistenceContext
        public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public <T> Object getUserByEmail(String course, String email) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE course = ?1 AND email = ?2", UserObject.class);
            query.setParameter(1, course);
            query.setParameter(2, email);
            List<UserObject> resultList = query.getResultList();
            if(!resultList.isEmpty()){
                return (UserObject) resultList.get(0);
            }else{
                return new RestResponse("Slack User does not exist for email :" + email + " and course :" + course);
            }
        }

    @Override
    public <T> Object getUserByDisplayName(String course, String display_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE course = ?1 AND display_name = ?2", UserObject.class);
        query.setParameter(1, course);
        query.setParameter(2, display_name);
        List<UserObject> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return (UserObject) resultList.get(0);
        }else{
            return new RestResponse("Slack User does not exist for display_name :" + display_name + " and course :" + course);
        }
    }

    @Override
    public <T> Object deleteUserByEmail(String course, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.slack_user WHERE course = ?1 AND email = ?2");
        query.setParameter(1, course);
        query.setParameter(2, email);
        query.executeUpdate();
        return new RestResponse("Slack User for email :" + email + " and course :" + course + "has been removed from the database");
    }

    @Override
    public <T> List<UserObject> getUsersByEmail(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE email = ?1", UserObject.class);
        query.setParameter(1, email);
        List<UserObject> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public <T> List<UserObject> getUsersByDisplayName(String display_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE display_name = ?1", UserObject.class);
        query.setParameter(1, display_name);
        List<UserObject> resultList = query.getResultList();
        return resultList;
    }
}
