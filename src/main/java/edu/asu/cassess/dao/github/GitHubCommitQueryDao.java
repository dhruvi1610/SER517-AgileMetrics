package edu.asu.cassess.dao.github;

import edu.asu.cassess.model.Taiga.WeeklyFreqWeight;
import edu.asu.cassess.model.Taiga.WeeklyIntervals;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class GitHubCommitQueryDao implements IGitHubCommitQueryDao {

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
    public RestResponse deleteCommitsByStudent(Student student) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.commit_data WHERE course = ?1 AND team = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse("github commits for student: " + student.getEmail() + " have been removed from the database");
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, date as weekBeginning, DATE_ADD(date, INTERVAL 6 DAY) as weekEnding,\n" +
                "IF (LOCA < LOCD, 1, IF(TOTC/(DAYOFWEEK(CURDATE()) * 16) > 3, 3, ROUND(TOTC/(DAYOFWEEK(CURDATE()) * 16),3))) AS weight,\n" +
                "IF(COMT/DAYOFWEEK(CURDATE()) > 3, 3, ROUND(COMT/DAYOFWEEK(CURDATE()),3)) AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "AVG(total_code) as TOTC, AVG(commits) as COMT, AVG(LOCA) AS LOCA, AVG(LOCD) AS LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/4) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "(students\n" +
                "inner join\n" +
                "commit_data)\n" +
                "WHERE students.email = commit_data.email)first,\n" +
                "(select @rn \\:= 0) var\n" +
                "WHERE course = ?1\n" +
                "GROUP BY date)second\n" +
                "ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, date as weekBeginning, DATE_ADD(date, INTERVAL 6 DAY) as weekEnding,\n" +
                "IF (LOCA < LOCD, 1, IF(TOTC/(DAYOFWEEK(CURDATE()) * 16) > 3, 3, ROUND(TOTC/(DAYOFWEEK(CURDATE()) * 16),3))) AS weight,\n" +
                "IF(COMT/DAYOFWEEK(CURDATE()) > 3, 3, ROUND(COMT/DAYOFWEEK(CURDATE()),3)) AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "AVG(total_code) as TOTC, AVG(commits) as COMT, AVG(LOCA) AS LOCA, AVG(LOCD) AS LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/4) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "(students\n" +
                "inner join\n" +
                "commit_data)\n" +
                "WHERE students.email = commit_data.email)first,\n" +
                "(select @rn \\:= 0) var\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "GROUP BY date)second\n" +
                "ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, date as weekBeginning, DATE_ADD(date, INTERVAL 6 DAY) as weekEnding,\n" +
                "IF (LOCA < LOCD, 1, IF(TOTC/(DAYOFWEEK(CURDATE()) * 16) > 3, 3, ROUND(TOTC/(DAYOFWEEK(CURDATE()) * 16),3))) AS weight,\n" +
                "IF(COMT/DAYOFWEEK(CURDATE()) > 3, 3, ROUND(COMT/DAYOFWEEK(CURDATE()),3)) AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "total_code as TOTC, commits as COMT, LOCA, LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/4) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "(students\n" +
                "inner join\n" +
                "commit_data)\n" +
                "WHERE students.email = commit_data.email)first,\n" +
                "(select @rn \\:= 0) var\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                "GROUP BY date)second\n" +
                "ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<CommitData> getCommitsByCourse(String course, String beginDate, String endDate) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, project_name, github_owner, email, course, team, AVG(commits) as commits, AVG(lines_of_code_added) as lines_of_code_added, AVG(lines_of_code_deleted) as lines_of_code_deleted\n" +
                "FROM \n" +
                "cassess.commit_data\n" +
                "WHERE course = ?1\n" +
                "AND date>=?2\n"+
                "AND date<=?3\n"+
                "GROUP BY date", CommitData.class);
        query.setParameter(1, course);
        query.setParameter(2, beginDate);
        query.setParameter(3, endDate);
        List<CommitData> resultList = query.getResultList();
        return resultList;
    }
   @Override
    @Transactional
    public List<CommitData> getCommitsByTeam(String course, String team, String beginDate, String endDate) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, project_name, github_owner, email, course, team, AVG(commits) as commits, AVG(lines_of_code_added) as lines_of_code_added, AVG(lines_of_code_deleted) as lines_of_code_deleted\n" +
                "FROM \n" +
                "cassess.commit_data\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND date >= ?3\n"+
                "AND date <=?4\n"+
                "GROUP BY date", CommitData.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, beginDate);
        query.setParameter(4, endDate);
        System.out.println("[LOG]: Begin Date for Team Commits "+beginDate + " and end date: "+endDate);
        List<CommitData> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<CommitData> getCommitsByStudent(String course, String team, String email, String beginDate, String endDate) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT date, username, project_name, github_owner, email, course, team, AVG(commits) as commits, AVG(lines_of_code_added) as lines_of_code_added, AVG(lines_of_code_deleted) as lines_of_code_deleted\n" +
                "FROM \n" +
                "cassess.commit_data\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                 "AND date >= ?4\n" +
                "AND date <=?5\n"+
                "GROUP BY date", CommitData.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        query.setParameter(4, beginDate);
        query.setParameter(5, endDate);
        List<CommitData> resultList = query.getResultList();
        return resultList;
    }
    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(date) as 'weekBeginning', DATE(date + INTERVAL (DAYOFWEEK(date) - 1) DAY) as 'weekEnding' FROM cassess.commit_data WHERE course = ?1 AND team = ?2 AND email = ?3 group by week(date)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(date) as 'weekBeginning', DATE(date + INTERVAL (DAYOFWEEK(date) - 1) DAY) as 'weekEnding' FROM cassess.commit_data WHERE course = ?1 AND team = ?2 group by week(date)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(date) as 'weekBeginning', DATE(date + INTERVAL (DAYOFWEEK(date) - 1) DAY) as 'weekEnding' FROM cassess.commit_data WHERE course = ?1 group by week(date)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }
}
