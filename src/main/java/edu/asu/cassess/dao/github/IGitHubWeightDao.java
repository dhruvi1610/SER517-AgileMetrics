package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.GitHubWeight;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;

/**
 * Created by Thomas on 4/18/2017.
 */
public interface IGitHubWeightDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    GitHubWeight getWeightByDate(String email, Date date);

    @Transactional
    List<GitHubWeight> getWeightByEmail(String email);
}
