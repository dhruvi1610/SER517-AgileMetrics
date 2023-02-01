package edu.asu.cassess.dao;

import edu.asu.cassess.persist.entity.UserID;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;

public interface IUserQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    UserID getUserID() throws DataAccessException;
}
