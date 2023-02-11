package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.Taiga.WeeklyFreqWeight;
import edu.asu.cassess.model.Taiga.WeeklyIntervals;
import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.model.slack.WeeklyMessageTotals;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Thomas on 4/23/2017.
 */
public interface ISlackMessageTotalsQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    RestResponse deleteMessagesByStudent(Student student) throws DataAccessException;

    @Transactional
    List<DailyMessageTotals> getDailyCountsByCourse(String beginDate, String endDate, String course) throws DataAccessException;

    @Transactional
    List<DailyMessageTotals> getDailyCountsByTeam(String beginDate, String endDate, String course, String team) throws DataAccessException;

    @Transactional
    List<DailyMessageTotals> getDailyCountsByStudent(String beginDate, String endDate, String course, String team, String email) throws DataAccessException;

    @Transactional
    List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String team, String email) throws DataAccessException;

    @Transactional
    List<WeeklyIntervals> getWeeklyIntervalsByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<WeeklyIntervals> getWeeklyIntervalsByCourse(String course) throws DataAccessException;

    @Transactional
    List<WeeklyMessageTotals> getWeeklyTotalsByCourse(String course) throws DataAccessException;

    @Transactional
    List<WeeklyMessageTotals> getWeeklyTotalsByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<WeeklyMessageTotals> getWeeklyTotalsByStudent(String course, String team, String email) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> twoWeekWeightFreqByCourse(String course) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> twoWeekWeightFreqByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> twoWeekWeightFreqByStudent(String course, String team, String email) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> weeklyWeightFreqByCourse(String course) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> weeklyWeightFreqByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> weeklyWeightFreqByStudent(String course, String team, String email) throws DataAccessException;
}
