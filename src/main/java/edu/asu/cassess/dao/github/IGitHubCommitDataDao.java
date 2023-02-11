package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.CommitData;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;

/**
 * Created by Thomas on 4/18/2017.
 */
public interface IGitHubCommitDataDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    CommitData getCommit(String email, Date date) throws DataAccessException;

    @Transactional
    List<CommitData> getCommitByEmail(String email);

    @Transactional
    List<CommitData> getAllCommitData() throws DataAccessException;
}
