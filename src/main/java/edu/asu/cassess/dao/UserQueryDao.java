package edu.asu.cassess.dao;

import edu.asu.cassess.persist.entity.UserID;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
@Transactional
public class UserQueryDao implements IUserQueryDao {

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
    public UserID getUserID() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COALESCE(MAX(id), 1) AS 'Max' FROM cassess.users", UserID.class);
        UserID result = (UserID) query.getSingleResult();
        return result;
    }

}
