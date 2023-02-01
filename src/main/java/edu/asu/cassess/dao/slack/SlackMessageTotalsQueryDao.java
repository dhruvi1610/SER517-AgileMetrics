package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.Taiga.WeeklyFreqWeight;
import edu.asu.cassess.model.Taiga.WeeklyIntervals;
import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.model.slack.WeeklyMessageTotals;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.repo.slack.SlackMessageTotalsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class SlackMessageTotalsQueryDao implements ISlackMessageTotalsQueryDao {

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
    public RestResponse deleteMessagesByStudent(Student student) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.slack_messagetotals WHERE course = ?1 AND team = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse("messages for student: " + student.getEmail() + " have been removed from the database");
    }

    @Override
    @Transactional
    public List<DailyMessageTotals> getDailyCountsByCourse(String beginDate, String endDate, String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', AVG(messageCount) as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM cassess.slack_messagetotals\n" +
                "WHERE retrievalDate >= ?1\n" +
                "AND retrievalDate <= ?2\n" +
                "AND course = ?3\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate", DailyMessageTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        List<DailyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<DailyMessageTotals> getDailyCountsByTeam(String beginDate, String endDate, String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', AVG(messageCount) as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM cassess.slack_messagetotals\n" +
                "WHERE retrievalDate >= ?1\n" +
                "AND retrievalDate <= ?2\n" +
                "AND course = ?3\n" +
                "AND team = ?4\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate", DailyMessageTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        query.setParameter(4, team);
        List<DailyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<DailyMessageTotals> getDailyCountsByStudent(String beginDate, String endDate, String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', messageCount as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM cassess.slack_messagetotals\n" +
                "WHERE retrievalDate >= ?1\n" +
                "AND retrievalDate <= ?2\n" +
                "AND course = ?3\n" +
                "AND team = ?4\n" +
                "AND email = ?5\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate",DailyMessageTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        query.setParameter(4, team);
        query.setParameter(5, email);
        List<DailyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.slack_messagetotals WHERE course = ?1 AND team = ?2 AND email = ?3 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.slack_messagetotals WHERE course = ?1 AND team = ?2 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.slack_messagetotals WHERE course = ?1 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyMessageTotals> getWeeklyTotalsByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekBeginning, \n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekEnding,\n" +
                "SUM(total) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, AVG(messageCount) as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning", WeeklyMessageTotals.class);
        query.setParameter(1, course);
        List<WeeklyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyMessageTotals> getWeeklyTotalsByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekBeginning, \n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekEnding,\n" +
                "SUM(total) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, AVG(messageCount) as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning", WeeklyMessageTotals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyMessageTotals> getWeeklyTotalsByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekBeginning, \n" +
                "UNIX_TIMESTAMP(DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY)) AS rawWeekEnding,\n" +
                "SUM(total) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, messageCount as total\n" +
                "FROM \n" +
                "(SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                "GROUP BY email, retrievalDate)inner0)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning", WeeklyMessageTotals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyMessageTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency," +
                "CASE\n" +
                        "WHEN total >= 200*(days/7) THEN 3\n" +
                        "WHEN total >= 100*(days/7) THEN 2\n" +
                        "WHEN total >= 50*(days/7)  THEN 1\n" +
                        "WHEN total <  50*(days/7)  THEN 0\n" +
                        "END AS weight\n" +
                        "FROM\n" +
                        "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                        "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                        "COUNT(retrievalDate) as 'days',\n" +
                        "if(messageCount<>0,1,0) as 'frequency',\n" +
                        "SUM(messageCount) as 'total'\n" +
                        "FROM\n" +
                        "(SELECT retrievalDate, AVG(messageCount) as messageCount\n" +
                        "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                        "FROM \n" +
                        "cassess.slack_messagetotals\n" +
                        "WHERE course = ?1\n" +
                        "GROUP BY email, retrievalDate)inner0\n" +
                        "GROUP BY retrievalDate)inner1,\n" +
                        "(select @rn \\:= 0) vars\n" +
                        "GROUP BY weekBeginning) outer1\n" +
                        "GROUP BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency, \n" +
                "CASE\n" +
                "WHEN total >= 200*(days/7) THEN 3\n" +
                "WHEN total >= 100*(days/7) THEN 2\n" +
                "WHEN total >= 50*(days/7)  THEN 1\n" +
                "WHEN total <  50*(days/7)  THEN 0\n" +
                "END AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "COUNT(retrievalDate) as 'days',\n" +
                "if(messageCount<>0,1,0) as 'frequency',\n" +
                "SUM(messageCount) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, AVG(messageCount) as messageCount\n" +
                "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning) outer1\n" +
                "GROUP BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency, \n" +
                "CASE\n" +
                "WHEN total >= 200*(days/7) THEN 3\n" +
                "WHEN total >= 100*(days/7) THEN 2\n" +
                "WHEN total >= 50*(days/7)  THEN 1\n" +
                "WHEN total <  50*(days/7)  THEN 0\n" +
                "END AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "COUNT(retrievalDate) as 'days',\n" +
                "if(messageCount<>0,1,0) as 'frequency',\n" +
                "SUM(messageCount) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, messageCount\n" +
                "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning) outer1\n" +
                "GROUP BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency, \n" +
                "CASE\n" +
                "WHEN total >= 200*(days/7) THEN 3\n" +
                "WHEN total >= 100*(days/7) THEN 2\n" +
                "WHEN total >= 50*(days/7)  THEN 1\n" +
                "WHEN total <  50*(days/7)  THEN 0\n" +
                "END AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "COUNT(retrievalDate) as 'days',\n" +
                "if(messageCount<>0,1,0) as 'frequency',\n" +
                "SUM(messageCount) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, AVG(messageCount) as messageCount\n" +
                "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning) outer1\n" +
                "GROUP BY week", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency, \n" +
                "CASE\n" +
                "WHEN total >= 200*(days/7) THEN 3\n" +
                "WHEN total >= 100*(days/7) THEN 2\n" +
                "WHEN total >= 50*(days/7)  THEN 1\n" +
                "WHEN total <  50*(days/7)  THEN 0\n" +
                "END AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "COUNT(retrievalDate) as 'days',\n" +
                "if(messageCount<>0,1,0) as 'frequency',\n" +
                "SUM(messageCount) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, AVG(messageCount) as messageCount\n" +
                "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning) outer1\n" +
                "GROUP BY week", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, ROUND((frequency/days)*3, 3) as frequency, \n" +
                "CASE\n" +
                "WHEN total >= 200*(days/7) THEN 3\n" +
                "WHEN total >= 100*(days/7) THEN 2\n" +
                "WHEN total >= 50*(days/7)  THEN 1\n" +
                "WHEN total <  50*(days/7)  THEN 0\n" +
                "END AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding',\n" +
                "COUNT(retrievalDate) as 'days',\n" +
                "if(messageCount<>0,1,0) as 'frequency',\n" +
                "SUM(messageCount) as 'total'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, messageCount\n" +
                "FROM (SELECT retrievalDate, email, fullName, course, team, channel_id, SUM(messageCount) as messageCount\n" +
                "FROM \n" +
                "cassess.slack_messagetotals\n" +
                "WHERE course = ?1\n" +
                "AND team = ?2\n" +
                "AND email = ?3\n" +
                "GROUP BY email, retrievalDate)inner0\n" +
                "GROUP BY retrievalDate)inner1,\n" +
                "(select @rn \\:= 0) vars\n" +
                "GROUP BY weekBeginning) outer1\n" +
                "GROUP BY week", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }


}
