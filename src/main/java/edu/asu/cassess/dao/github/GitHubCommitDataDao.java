package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.CommitData;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubCommitDataDao implements IGitHubCommitDataDao {
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
    @Transactional
    public CommitData getCommit(String email, Date date) throws DataAccessException{

        CommitData commitData;
        try{
            //noinspection JpaQlInspection
            commitData = (CommitData) entityManager
                    .createQuery("SELECT c FROM CommitData c  WHERE c.email = ?1 AND c.date = ?2")
                    .setParameter(1, email)
                    .setParameter(2, date)
                    .getSingleResult();
        }catch(Exception e){
            commitData = null;
        }

        return commitData;
    }

    @Override
    @Transactional
    public List<CommitData> getCommitByEmail(String email){
        List<CommitData> commitData;
        try{
            //noinspection JpaQlInspection
            commitData = (List<CommitData>) entityManager
                    .createQuery("SELECT c FROM CommitData c  WHERE c.email = ?1")
                    .setParameter(1, email)
                    .getResultList();
        }catch(Exception e){
            commitData = new ArrayList();
        }

        return commitData;
    }

    @Override
    @Transactional
    public List<CommitData> getAllCommitData() throws DataAccessException{
        //noinspection JpaQlInspection
        return entityManager
                    .createQuery("SELECT c FROM CommitData c")
                    .getResultList();
    }

}
