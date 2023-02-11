package edu.asu.cassess.dao.github;


import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

@Component
public class GitHubWeightQueryDao implements IGitHubWeightQueryDao {

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
    public RestResponse deleteWeightsByStudent(Student student) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.github_weight WHERE course = ?1 AND team = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse("github weights for student: " + student.getEmail() + " have been removed from the database");
    }

    @Override
    @Transactional
    public List<GitHubWeight> getWeightsByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, email, course, team, AVG(weight) as weight\n" +
                "FROM\n" +
                "cassess.github_weight\n" +
                "WHERE course = ?1\n" +
                "GROUP BY date", GitHubWeight.class);
        query.setParameter(1, course);
        List<GitHubWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<GitHubWeight> getWeightsByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, email, course, team, AVG(weight) as weight\n" +
                "FROM\n" +
                "cassess.github_weight\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "GROUP BY date", GitHubWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<GitHubWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<GitHubWeight> getWeightsByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, email, course, team, AVG(weight) as weight\n" +
                "FROM\n" +
                "cassess.github_weight\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                "GROUP BY date", GitHubWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<GitHubWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public GitHubWeight getlastDate(String course, String team, String username) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT course, " +
                "DATE_SUB(max(date), INTERVAL 7 DAY) as date, team, username, email, weight " +
                "FROM cassess.github_weight\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND username = ?3\n", GitHubWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, username);
        List results = query.getResultList();
        GitHubWeight ghWeightLastDate = null;
        if(!results.isEmpty()){
            ghWeightLastDate = (GitHubWeight) results.get(0);
            return ghWeightLastDate;
        } else {
            return null;
        }
    }
}
