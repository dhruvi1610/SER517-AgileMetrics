package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Thomas on 4/25/2017.
 */
public interface IGitHubWeightQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    RestResponse deleteWeightsByStudent(Student student) throws DataAccessException;

    @Transactional
    List<GitHubWeight> getWeightsByCourse(String course) throws DataAccessException;

    @Transactional
    List<GitHubWeight> getWeightsByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<GitHubWeight> getWeightsByStudent(String course, String team, String email) throws DataAccessException;

    @Transactional
    GitHubWeight getlastDate(String course, String team, String username) throws DataAccessException;
}
