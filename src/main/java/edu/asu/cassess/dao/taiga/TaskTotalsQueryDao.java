package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class TaskTotalsQueryDao implements ITaskTotalsQueryDao {

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
    public List<TaskTotals> getTaskTotals() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.tasktotals", TaskTotals.class);
        List<TaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    @Modifying
    public RestResponse deleteTaskTotalsByCourse(Course course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.tasktotals WHERE course = ?1");
        query.setParameter(1, course.getCourse());
        query.executeUpdate();
        return new RestResponse("tasktotals for course: " + course.getCourse() + " have been removed from the database");
    }

    @Override
    @Transactional
    public RestResponse deleteTaskTotalsByProject(Team team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.tasktotals WHERE course = ?1 AND team = ?2");
        query.setParameter(1, team.getCourse());
        query.setParameter(2, team.getTeam_name());
        query.executeUpdate();
        return new RestResponse("tasktotals for team: " + team.getTeam_name() + " have been removed from the database");
    }

    @Override
    @Transactional
    public RestResponse deleteTaskTotalsByStudent(Student student) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.tasktotals WHERE course = ?1 AND team = ?2 AND email = ?3");
        query.setParameter(1, student.getCourse());
        query.setParameter(2, student.getTeam_name());
        query.setParameter(3, student.getEmail());
        query.executeUpdate();
        return new RestResponse("tasktotals for student: " + student.getEmail() + " have been removed from the database");
    }

    @Override
    @Transactional
    public List<DailyTaskTotals> getDailyTasksByCourse(String beginDate, String endDate, String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', AVG(tasksInProgress) as 'InProgress', AVG(tasksReadyForTest) as 'ToTest', AVG(tasksClosed) as 'Done' FROM cassess.tasktotals WHERE retrievalDate >= ?1 AND retrievalDate <= ?2 AND course = ?3 GROUP BY retrievalDate", DailyTaskTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        List<DailyTaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<DailyTaskTotals> getDailyTasksByTeam(String beginDate, String endDate, String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', AVG(tasksInProgress) as 'InProgress', AVG(tasksReadyForTest) as 'ToTest', AVG(tasksClosed) as 'Done' FROM cassess.tasktotals WHERE retrievalDate >= ?1 AND retrievalDate <= ?2 AND course = ?3 AND team = ?4 GROUP BY retrievalDate", DailyTaskTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        query.setParameter(4, team);
        List<DailyTaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<DailyTaskTotals> getDailyTasksByStudent(String beginDate, String endDate, String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', tasksInProgress as 'InProgress', tasksReadyForTest as 'ToTest', tasksClosed as 'Done' FROM cassess.tasktotals WHERE retrievalDate >= ?1 AND retrievalDate <= ?2 AND course = ?3 AND team = ?4 AND email = ?5 GROUP BY retrievalDate", DailyTaskTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, course);
        query.setParameter(4, team);
        query.setParameter(5, email);
        List<DailyTaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.tasktotals WHERE course = ?1 AND team = ?2 AND email = ?3 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.tasktotals WHERE course = ?1 AND team = ?2 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyIntervals> getWeeklyIntervalsByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.tasktotals WHERE course = ?1 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
        query.setParameter(1, course);
        List<WeeklyIntervals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyActivity> getWeeklyUpdatesByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding,\n" +
                "                ROUND(AVG(DoneActivity), 3) as 'DoneActivity', ROUND(AVG(InProgressActivity), 3) as 'InProgressActivity', ROUND(AVG(ToTestActivity), 3) as 'ToTestActivity'\n" +
                "                FROM\n" +
                "                (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', SUM(tasksClosedDIFF) as 'DoneActivity', \n" +
                "                SUM(tasksInProgressDIFF) as 'InProgressActivity', SUM(tasksReadyForTestDIFF) as 'ToTestActivity'\n" +
                "                FROM\n" +
                "                (select\n" +
                "                      TSK.retrievalDate,\n" +
                "\t\t\t      TSK.fullName,\n" +
                "\t\t\t      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                               @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0,\n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                AND team = ?2\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "                      (select @rn \\:= 0) vars\n" +
                "                      GROUP BY weekBeginning", WeeklyActivity.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyActivity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyActivity> getWeeklyUpdatesByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, DoneActivity, InProgressActivity, ToTestActivity, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding\n" +
                "FROM\n" +
                "                               (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning',\n" +
                "                                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', SUM(tasksClosedDIFF) as 'DoneActivity', \n" +
                "\t\t\t\t\t\t\tSUM(tasksInProgressDIFF) as 'InProgressActivity', SUM(tasksReadyForTestDIFF) as 'ToTestActivity'\n" +
                "                                FROM\n" +
                "                                (select\n" +
                "                                      TSK.retrievalDate,\n" +
                "\t\t\t\t\t\t\t\t\t\tTSK.fullName,\n" +
                "\t\t\t\t\t\t\t\t\t  if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                                      @lastfullName \\:= TSK.fullName,\n" +
                "                                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                                   from\n" +
                "                                      tasktotals TSK,\n" +
                "                                      ( select @lastfullName \\:= 0,\n" +
                "                                               @lasttasksClosed \\:= 0,\n" +
                "                                               @lasttasksInProgress \\:= 0,\n" +
                "                                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                                WHERE course = ?1\n" +
                "                                AND team = ?2\n" +
                "                                AND email = ?3\n" +
                "                                   order by\n" +
                "                                      TSK.fullName,\n" +
                "                                      TSK.retrievalDate) query1\n" +
                "                                      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "                                      (select @rn \\:= 0) vars\n" +
                "                                      GROUP BY weekBeginning", WeeklyActivity.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyActivity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyActivity> getWeeklyUpdatesByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, UNIX_TIMESTAMP(weekBeginning) AS rawWeekBeginning, UNIX_TIMESTAMP(weekEnding) AS rawWeekEnding,\n" +
                "                ROUND(AVG(DoneActivity), 3) as 'DoneActivity', ROUND(AVG(InProgressActivity), 3) as 'InProgressActivity', ROUND(AVG(ToTestActivity), 3) as 'ToTestActivity'\n" +
                "                FROM\n" +
                "                (SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', SUM(tasksClosedDIFF) as 'DoneActivity', \n" +
                "                SUM(tasksInProgressDIFF) as 'InProgressActivity', SUM(tasksReadyForTestDIFF) as 'ToTestActivity'\n" +
                "                FROM\n" +
                "                (select\n" +
                "                      TSK.retrievalDate,\n" +
                "\t\t\t      TSK.fullName,\n" +
                "\t\t\t      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                               @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0,\n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "                      (select @rn \\:= 0) vars\n" +
                "                      GROUP BY weekBeginning", WeeklyActivity.class);
        query.setParameter(1, course);
        List<WeeklyActivity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(SUM(NULLIF(Done ,0)), 0) as 'Done', COALESCE(SUM(NULLIF(InProgress ,0)), 0) as 'InProgress', COALESCE(SUM(NULLIF(ToTest ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(SUM(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(SUM(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(SUM(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', SUM(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', tasksClosedDIFF, tasksInProgressDiff, tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                AND team = ?2\n" +
                "                AND email = ?3\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(NULLIF(Done ,0), 0) as 'Done', COALESCE(NULLIF(InProgress ,0), 0) as 'InProgress', COALESCE(NULLIF(ToTest ,0), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', AVG(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', \n" +
                "SUM(tasksClosedDIFF) AS tasksClosedDIFF, SUM(tasksInProgressDIFF) AS tasksInProgressDIFF, SUM(tasksReadyForTestDIFF) AS tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                AND team = ?2\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> twoWeekWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(NULLIF(Done ,0), 0) as 'Done', COALESCE(NULLIF(InProgress ,0), 0) as 'InProgress', COALESCE(NULLIF(ToTest ,0), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', AVG(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', \n" +
                "SUM(tasksClosedDIFF) AS tasksClosedDIFF, SUM(tasksInProgressDIFF) AS tasksInProgressDIFF, SUM(tasksReadyForTestDIFF) AS tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(SUM(NULLIF(Done ,0)), 0) as 'Done', COALESCE(SUM(NULLIF(InProgress ,0)), 0) as 'InProgress', COALESCE(SUM(NULLIF(ToTest ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(SUM(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(SUM(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(SUM(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', SUM(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', tasksClosedDIFF, tasksInProgressDiff, tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                AND team = ?2\n" +
                "                AND email = ?3\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(NULLIF(Done ,0), 0) as 'Done', COALESCE(NULLIF(InProgress ,0), 0) as 'InProgress', COALESCE(NULLIF(ToTest ,0), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', AVG(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', \n" +
                "SUM(tasksClosedDIFF) AS tasksClosedDIFF, SUM(tasksInProgressDIFF) AS tasksInProgressDIFF, SUM(tasksReadyForTestDIFF) AS tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                AND team = ?2\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> weeklyWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT week, weekBeginning, weekEnding, IF(frequency > 3, 3, frequency) as 'frequency', IF((GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1))) > 3, 3, ROUND(GREATEST(Done/(days + 1), InProgress/(days + 1), ToTest/(days + 1)), 3)) AS weight\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', days, weekBeginning, weekEnding, ROUND(frequency/daysFreq, 3) as 'frequency', \n" +
                "COALESCE(NULLIF(Done ,0), 0) as 'Done', COALESCE(NULLIF(InProgress ,0), 0) as 'InProgress', COALESCE(NULLIF(ToTest ,0), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT days, COUNT(retrievalDate) as daysFreq, (retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "                DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', \n" +
                "                SUM(frequency) as 'frequency', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'Done', \n" +
                "                COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgress', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTest'\n" +
                "FROM\n" +
                "(SELECT retrievalDate, COUNT(retrievalDate) as 'days', AVG(IF(tasksClosedDIFF<>0,1,0) + IF(tasksInProgressDIFF<>0,1,0) + IF(tasksReadyForTestDIFF<>0,1,0)) as 'frequency', \n" +
                "SUM(tasksClosedDIFF) AS tasksClosedDIFF, SUM(tasksInProgressDIFF) AS tasksInProgressDIFF, SUM(tasksReadyForTestDIFF) AS tasksReadyForTestDIFF\n" +
                "FROM\n" +
                "(select\n" +
                "                      TSK.retrievalDate,\n" +
                "                      TSK.fullName,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "                      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "                      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "                      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "                      @lastfullName \\:= TSK.fullName,\n" +
                "                      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "                   from\n" +
                "                      tasktotals TSK,\n" +
                "                      ( select @lastfullName \\:= 0,\n" +
                "                   @lasttasksClosed \\:= 0,\n" +
                "                               @lasttasksInProgress \\:= 0, \n" +
                "                               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "                WHERE course = ?1\n" +
                "                   order by\n" +
                "                      TSK.fullName,\n" +
                "                      TSK.retrievalDate) query1\n" +
                "                      GROUP BY retrievalDate) query2,\n" +
                "                      (select @rn \\:= 0) var\n" +
                "                      GROUP BY weekBeginning) query3 \n" +
                "                      GROUP BY weekBeginning) query4\n" +
                "                      GROUP BY weekBeginning", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> getWeeklyAverageByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning", WeeklyAverages.class);
        query.setParameter(1, course);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> getWeeklyAverageByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "    AND project = ?2\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning", WeeklyAverages.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> getWeeklyAverageByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "    AND project = ?2\n" +
                "    AND email = ?3\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning", WeeklyAverages.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> lastTwoWeekAveragesByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning" +
                "       ORDER BY week DESC LIMIT 2", WeeklyAverages.class);
        query.setParameter(1, course);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> lastTwoWeekAveragesByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest := TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "    AND project = ?2\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning" +
                "       ORDER BY week DESC LIMIT 2", WeeklyAverages.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyAverages> lastTwoWeekAveragesByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, ROUND(COALESCE(AVG(NULLIF(DoneAverage ,0)), 0), 0) as 'DoneAverage', ROUND(COALESCE(AVG(NULLIF(InProgressAverage ,0)), 0), 0) as 'InProgressAverage', ROUND(COALESCE(AVG(NULLIF(ToTestAverage ,0)), 0), 0) as 'ToTestAverage'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (0 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', \n" +
                "DATE(retrievalDate + INTERVAL (6 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', COALESCE(AVG(NULLIF(tasksClosedDIFF ,0)), 0) as 'DoneAverage', \n" +
                "COALESCE(AVG(NULLIF(tasksInProgressDIFF ,0)), 0) as 'InProgressAverage', COALESCE(AVG(NULLIF(tasksReadyForTestDIFF ,0)), 0) as 'ToTestAverage'\n" +
                "FROM(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.fullName,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksClosed - @lasttasksClosed), TSK.tasksClosed) as tasksClosedDIFF,\n" +
                "      @lasttasksClosed \\:= TSK.tasksClosed,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest := TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "\t\t\t   @lasttasksClosed \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE course = ?1\n" +
                "    AND project = ?2\n" +
                "    AND email = ?2\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "       GROUP By weekBeginning" +
                "       ORDER BY week DESC LIMIT 2", WeeklyAverages.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyAverages> resultList = query.getResultList();
        return resultList;
    }
}
