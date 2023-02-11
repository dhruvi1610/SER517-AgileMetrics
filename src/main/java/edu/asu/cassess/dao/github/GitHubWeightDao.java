package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.GitHubWeight;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubWeightDao implements IGitHubWeightDao {
    protected EntityManager entityManager;

    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public GitHubWeight getWeightByDate(String email, Date date){
        GitHubWeight weight;

        try{
            //noinspection JpaQlInspection
            weight = (GitHubWeight) entityManager
                    .createQuery("SELECT w " +
                                         "FROM GitHubWeight " +
                                         "WHERE w.email = ?1 AND w.date = ?2")
                    .setParameter(1, email)
                    .setParameter(2, date)
                    .getSingleResult();
        }catch(Exception e){
            weight = null;
        }

        return weight;
    }

    @Override
    @Transactional
    public List<GitHubWeight> getWeightByEmail(String email){
        List<GitHubWeight> weight;

        try{
            //noinspection JpaQlInspection
            weight = (List<GitHubWeight>) entityManager
                    .createQuery("SELECT w FROM GitHubWeight w WHERE w.email =?1")
                    .setParameter(1, email)
                    .getResultList();
        }catch(Exception e){
            e.printStackTrace();
            weight = new ArrayList();
        }
        return weight;
    }

}
